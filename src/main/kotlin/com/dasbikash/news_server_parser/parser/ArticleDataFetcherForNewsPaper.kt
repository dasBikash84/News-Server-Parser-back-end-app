/*
 * Copyright 2019 das.bikash.dev@gmail.com. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasbikash.news_server_parser.parser

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.exceptions.*
import com.dasbikash.news_server_parser.exceptions.handler.ParserExceptionHandler
import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.LoggerUtils
import org.hibernate.Session
import java.net.URISyntaxException
import java.util.*
import kotlin.random.Random

class ArticleDataFetcherForNewsPaper(
        val newspaper: Newspaper, //caller must end session before sending Newspaper object
        val opMode: ParserMode
) : Thread() {

    private var lastNetworkRequestTS = 0L
    private val MIN_DELAY_BETWEEN_NETWORK_REQUESTS = 5 * 1000L //5 secs
    private val PAGE_NUMBER_NOT_APPLICABLE = 0

    private val SQL_FOR_LAST_PARSED_PAGE_NUMBER = "FROM PageParsingHistory where pageId=:currentPageId order by created desc"

    private val topLevelPages = mutableListOf<Page>()
    private val childPageMap = mutableMapOf<Page, ArrayList<Page>>()
    lateinit var dbSession: Session

    private val ALL_PAGE_PARSING_PERIOD = 3 * 60 * 60 * 1000L // 3 hours

    override fun run() {
        sleep(Random(System.currentTimeMillis()).nextLong(MIN_DELAY_BETWEEN_NETWORK_REQUESTS))
        println("Parser for ${newspaper.name} started at ${Date()}")
        LoggerUtils.logMessage("Parser for ${newspaper.name} started", getDatabaseSession())

        getDatabaseSession().update(newspaper) //take newspaper in active state

        newspaper.pageList?.sortedBy { it.id }
                ?.asSequence()
                ?.filter { it.isTopLevelPage() }
                ?.toCollection(topLevelPages)

        topLevelPages.asSequence()
                .forEach {
                    val topLevelPage = it
                    val childPageList = ArrayList<Page>()
                    newspaper.pageList
                            ?.filter { it.parentPageId == topLevelPage.id && it.hasData() }
                            ?.toCollection(childPageList)
                    childPageMap.put(it, childPageList)
                }

        var maxChildPageCountForPage = 0

        childPageMap.values.forEach({
            if (maxChildPageCountForPage < it.size) {
                maxChildPageCountForPage = it.size
            }
        }) //get maximum child count

        val pageListForParsing = mutableListOf<Page>()

        //add active top level pages to parcable page list
        topLevelPages.forEach {
            if (it.hasData()) { //if only point to page
                pageListForParsing.add(it)
            }
        }

        var i = 0

        while (i < maxChildPageCountForPage) {
            childPageMap.values.forEach {
                if (it.size > i) {
                    val page = it.get(i)
                    if (page.hasData()) {
                        pageListForParsing.add(page)
                    }
                }
            }
            i++
        }
        pageListForParsing.asSequence()
                .forEach {
                    emptyPageAction(it, "Reset on start.")
                }

        val delayBetweenPages = ALL_PAGE_PARSING_PERIOD / pageListForParsing.size

        do {
            //Mark pages with articles as active
            pageListForParsing.asSequence()
                    .forEach {
                        if (!it.active) {

                            if (!it.isTopLevelPage()) {
                                if (it.articleList != null) {
                                    it.active = DatabaseUtils.getArticleCountForPage(getDatabaseSession(), it.id) > 0
                                } else {
                                    it.active = false
                                }
                            } else {
                                it.active = true
                            }
                            if (it.active) {
                                DatabaseUtils.runDbTransection(getDatabaseSession()) {
                                    getDatabaseSession().update(it)
                                }
                            }
                        }
                    }

            println("Going to parse ${pageListForParsing.size} pages for Np: ${newspaper.name}")

            val shuffledPages = pageListForParsing.shuffled()

            for (currentPage in shuffledPages) {

                try {
                    if (opMode == ParserMode.RUNNING) {
                        sleep(delayBetweenPages)
                    }else{
                        waitForFareNetworkUsage()
                    }
                } catch (ex: InterruptedException) {
                    println("Exiting ${this::class.java.simpleName} for ${newspaper.name}")
                    return
                }

                val currentPageNumber: Int

                if (currentPage.isPaginated()) {
                    currentPageNumber = getLastParsedPageNumber(currentPage) + 1
                } else {
                    currentPageNumber = PAGE_NUMBER_NOT_APPLICABLE
                }

                val articleList: MutableList<Article> = mutableListOf()

                var parsingResult: Pair<MutableList<Article>, String>? = null
                try {
                    parsingResult = PreviewPageParser.parsePreviewPageForArticles(currentPage, currentPageNumber)
                    articleList.addAll(parsingResult.first)
                } catch (e: NewsPaperNotFoundForPageException) {
                    println("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                    ParserExceptionHandler.handleException(e)
                    println("Exiting ${this::class.java.simpleName} for ${newspaper.name}")
                    return
                } catch (e: ParserNotFoundException) {
                    println("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                    ParserExceptionHandler.handleException(e)
                    println("Exiting ${this::class.java.simpleName} for ${newspaper.name}")
                    return
                } catch (e: PageLinkGenerationException) {
                    println("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                    ParserExceptionHandler.handleException(e)
                    continue
                } catch (e: URISyntaxException) {
                    println("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                    ParserExceptionHandler.handleException(ParserException(e))
                    continue
                } catch (e: EmptyJsoupDocumentException) {
                    println("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                    ParserExceptionHandler.handleException(e)
                    continue
                } catch (e: EmptyArticlePreviewPageException) {
                    println("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                    ParserExceptionHandler.handleException(e)
                    if (opMode == ParserMode.RUNNING) {
                        emptyPageAction(currentPage, parsingResult?.second ?: "")
                    }
                    continue
                } catch (e: Throwable) {
                    println("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                    ParserExceptionHandler.handleException(ParserException(e))
                    continue
                }

                val parseableArticleList =
                        articleList
                                .asSequence()
                                .filter {
                                    DatabaseUtils.findArticleById(getDatabaseSession(), it.id) == null
                                }
                                .toCollection(mutableListOf())
                //For Full repeat
                if (opMode == ParserMode.RUNNING && parseableArticleList.size == 0) {
                    allArticleRepeatAction(currentPage, parsingResult?.second ?: "")
                    continue
                }

                //Now go for article data fetching

                for (article in parseableArticleList) {
                    try {
                        waitForFareNetworkUsage()
                    } catch (ex: InterruptedException) {
                        ex.printStackTrace()
                        println("Exiting ${this::class.java.simpleName} for ${newspaper.name}")
                        return
                    }
                    try {
                        ArticleBodyParser.getArticleBody(article)
                        if (article.isDownloaded()) {
                            if (article.previewImageLink == null && article.imageLinkList.size > 0) {
                                article.previewImageLink = article.imageLinkList.get(0).link
                            }
                            DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().save(article) }
                        }
                    } catch (ex: ParserException) {
                        ParserExceptionHandler.handleException(ex)
                    } catch (ex: Throwable) {
                        ParserExceptionHandler.handleException(ParserException(ex))
                    }
                }

                savePageParsingHistory(
                        currentPage, currentPageNumber, parseableArticleList.size, parsingResult?.second ?: "")
            }
        } while (pageListForParsing.size > 0)
    }

    private fun emptyPageAction(currentPage: Page, parsingLogMessage: String) {
        savePageParsingHistory(currentPage, 0, 0, "No article found." + parsingLogMessage)
    }

    private fun allArticleRepeatAction(currentPage: Page, parsingLogMessage: String) {
        savePageParsingHistory(currentPage, 0, 0, "All articles are repeated." + parsingLogMessage)
    }

    private fun savePageParsingHistory(currentPage: Page, currentPageNumber: Int, articleCount: Int, parsingLogMessage: String = "") {
        if (articleCount > 0) {
            println("${articleCount} new article found for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
        } else {
            println("No new article found for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
        }
        DatabaseUtils.runDbTransection(getDatabaseSession()) {
            getDatabaseSession().save(PageParsingHistory(page = currentPage, pageNumber = currentPageNumber,
                    articleCount = articleCount, parsingLogMessage = parsingLogMessage))
        }
    }

    fun waitForFareNetworkUsage() {

        val ramdomDelay = Random(System.currentTimeMillis()).nextLong(MIN_DELAY_BETWEEN_NETWORK_REQUESTS)

        var delayPeriod = MIN_DELAY_BETWEEN_NETWORK_REQUESTS -
                (System.currentTimeMillis() - lastNetworkRequestTS)

        if (delayPeriod > 0) {
            delayPeriod += ramdomDelay
        } else {
            delayPeriod = ramdomDelay
        }

        sleep(delayPeriod)

        lastNetworkRequestTS = System.currentTimeMillis()
    }

    private fun getLastParsedPageNumber(page: Page): Int {
        val query = getDatabaseSession().createQuery(SQL_FOR_LAST_PARSED_PAGE_NUMBER)
        query.setParameter("currentPageId", page.id)
        try {
            val historyEntry = query.list().first() as PageParsingHistory
            return historyEntry.pageNumber
        } catch (ex: Exception) {
            return 0
        }
    }

    private fun getDatabaseSession(): Session {
        if (!::dbSession.isInitialized || !dbSession.isOpen) {
            dbSession = DbSessionManager.getNewSession()
        }
        return dbSession
    }
}