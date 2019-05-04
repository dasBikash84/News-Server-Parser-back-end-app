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

import com.dasbikash.news_server_parser.ParserMode
import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbNamedNativeQueries
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.exceptions.*
import com.dasbikash.news_server_parser.model.Article
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.model.Page
import com.dasbikash.news_server_parser.model.PageParsingHistory
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.LoggerUtils
import org.hibernate.Session
import java.net.URISyntaxException
import java.util.*
import kotlin.random.Random

class ArticleDataFeatcherForNewsPaper(
        private val newspaper: Newspaper, //caller must end session before sending Newspaper object
        private val opMode: ParserMode
) : Runnable {

    private val MAX_OLD_ARTICLE_DAYS = 30L
    private val MAX_OLD_ARTICLE_MS = MAX_OLD_ARTICLE_DAYS * 24 * 60 * 60 * 1000

    private var lastNetworkRequestTS = 0L
    private val MIN_DELAY_BETWEEN_NETWORK_REQUESTS = 10000L
    private val NOT_APPLICABLE_PAGE_NUMBER = 0

    private val topLevelPages = mutableListOf<Page>()
    private val childPageMap = mutableMapOf<Page, ArrayList<Page>>()
    lateinit var dbSession: Session

    override fun run() {
        println("Article data parser for ${newspaper.name} started at ${Date()}")
        Thread.sleep(Random(System.currentTimeMillis()).nextLong(MIN_DELAY_BETWEEN_NETWORK_REQUESTS))

        getDatabaseSession().update(newspaper) //take newspaper in active state

        newspaper.pageList
                ?.sortedBy {
                    it.id
                }
                ?.asSequence()
                ?.filter { it.isTopLevelPage() }//status will be checked prior to parsing
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

        do {
            //Mark pages with article as active
            pageListForParsing.asSequence()
                    .forEach {
                        if (!it.isTopLevelPage()) {
                            if (it.articleList != null) {
                                it.active = it.articleList!!.size > 0
                            } else {
                                it.active = false
                            }
                        }else{
                            it.active = true
                        }
                        DatabaseUtils.runDbTransection(getDatabaseSession()){
                            getDatabaseSession().update(it)
                        }
                    }

            val shuffledPageList = ArrayList<Page>(pageListForParsing)

            println("#############################################################################################")
            println("#############################################################################################")
            println("Going to parse ${pageListForParsing.size} pages:")

            for (currentPage in shuffledPageList) {

                println("Page Name: ${currentPage.name} Page Id: ${currentPage.id} ParentPage: " +
                        "${currentPage.parentPageId} Newspaper: ${currentPage.newspaper?.name}")


                val currentPageNumber: Int

                if (currentPage.isPaginated()) {
                    if (opMode == ParserMode.GET_SYNCED) {
                        currentPageNumber = getLastParsedPageNumber(currentPage) + 1
                    }else{
                        currentPageNumber = 1
                    }
                } else {
                    currentPageNumber = NOT_APPLICABLE_PAGE_NUMBER
                }

                waitForFareNetworkUsage()

                val articleList: MutableList<Article> = mutableListOf()


                var parsingResult: Pair<MutableList<Article>, String>? = null
                try {
                    parsingResult = PreviewPageParser.parsePreviewPageForArticles(currentPage, currentPageNumber)
                    articleList.addAll(parsingResult.first)
                } catch (e: NewsPaperNotFoundForPageException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    return
                } catch (e: ParserNotFoundException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    return
                } catch (e: PageLinkGenerationException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: EmptyJsoupDocumentException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: EmptyArticlePreviewPageException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession()) //to be removed
                    if (opMode == ParserMode.RUNNING) {
                        savePageParsingHistory(currentPage, currentPageNumber, 0, parsingResult?.second ?: "")
                        continue
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    LoggerUtils.logError(NewsServerParserException("Exp ${e::class.java.canonicalName} Msg: ${e.message} for page: ${currentPage.name} page no.: ${currentPageNumber} Np: ${currentPage.newspaper!!.name}"), getDatabaseSession())
                    continue
                }


                val parseableArticleList =
                        articleList
                        .asSequence()
                        .filter {
                            DatabaseUtils.findArticleById(getDatabaseSession(),it.id) == null
                        }
                        .toCollection(mutableListOf<Article>())
                //For Full repeat
                if (opMode == ParserMode.RUNNING && parseableArticleList.size == 0) {
                    allArticleRepeatAction(currentPage, parsingResult?.second ?: "")
                    continue
                }

                //Now go for article data fetching
                parseableArticleList
                        .asSequence()
                        .filter {
                            return@filter try {
                                waitForFareNetworkUsage()
                                println("Article before parsing:" + it)
                                ArticleBodyParser.getArticleBody(it)
                                true
                            } catch (ex: NewsServerParserException) {
                                ex.printStackTrace()
                                LoggerUtils.logError(ex, getDatabaseSession())
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                                false
                            } /*catch (ex: EmptyJsoupDocumentException) {
                                LoggerUtils.logError(ex,getDatabaseSession())
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                                false
                            } catch (ex: EmptyArticleBodyException) {
                                LoggerUtils.logError(ex,getDatabaseSession())
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                                false
                            } catch (ex: ArticleModificationTimeNotFoundException) {
                                LoggerUtils.logError(ex,getDatabaseSession())
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                                false
                            } */ catch (ex: Throwable) {
                                LoggerUtils.logError(NewsServerParserException("Exp: ${ex::class.java.canonicalName} Msg: ${ex.message} for article: ${it.articleLink}"), getDatabaseSession())
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                                false
                            }
                        }
                        .forEach {
                            println("Article after parsing:" + it)
                            if (it.isDownloaded()) {
                                if (it.previewImageLink == null && it.imageLinkList.size>0){
                                    it.previewImageLink = it.imageLinkList.get(0).link
                                }
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().save(it) }
                            }
                        }

                savePageParsingHistory(currentPage, currentPageNumber, parseableArticleList.size, parsingResult?.second ?: "")
            }
            println("#############################################################################################")
            println("#############################################################################################")
        } while (pageListForParsing.size > 0)
    }

    private fun allArticleRepeatAction(currentPage: Page, parsingLogMessage: String) {
        savePageParsingHistory(currentPage, 0, 0, "All articles are repeated." + parsingLogMessage)
    }

    private fun savePageParsingHistory(currentPage: Page, currentPageNumber: Int, articleCount: Int, parsingLogMessage: String = "") {
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

        try {
            Thread.sleep(delayPeriod)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
//        while ((System.currentTimeMillis() - lastNetworkRequestTS) < MIN_DELAY_BETWEEN_NETWORK_REQUESTS)
        lastNetworkRequestTS = System.currentTimeMillis()
    }

    private fun getLastParsedPageNumber(page: Page): Int {
        val query = getDatabaseSession().createQuery("FROM PageParsingHistory where pageId=:currentPageId order by created desc")
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
