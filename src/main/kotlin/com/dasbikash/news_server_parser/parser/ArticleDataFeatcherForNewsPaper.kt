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
        private val newspaper: Newspaper //caller must end session before sending Newspaper object
) : Runnable {


    private var lastNetworkRequestTS = 0L
    private val MIN_DELAY_BETWEEN_NETWORK_REQUESTS = 1000L
    private val NOT_APPLICABLE_PAGE_NUMBER = 0

    private val topLevelPages = mutableListOf<Page>()
    private val childPageMap = mutableMapOf<Page, ArrayList<Page>>()
    lateinit var dbSession: Session

    override fun run() {
        println("Article data parser for ${newspaper.name} started at ${Date()}")
        Thread.sleep(Random(System.currentTimeMillis()).nextLong(MIN_DELAY_BETWEEN_NETWORK_REQUESTS))

        getDatabaseSession().update(newspaper) //take newspaper in active state

        newspaper.pageList
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

        getDatabaseSession().close()


        //Before going into parsing loop first fetch and save any un-parsed article data of privious loop

        /*val unParsedArticleList: List<Article> = getUnParsedArticleOfCurrentNewspaper(newspaper)
        unParsedArticleList
                .asSequence()
                .filter {
                    return@filter try {
                        waitForFareNetworkUsage()
                        println("Article before parsing:" + it)
                        ArticleBodyParser.getArticleBody(it)
                        true
                    } catch (ex: EmptyArticleLinkException) {
                        LoggerUtils.logError(ex,getDatabaseSession())
                        DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                        false
                    } catch (ex: EmptyDocumentException) {
                        LoggerUtils.logError(ex,getDatabaseSession())
                        DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                        false
                    } catch (ex: EmptyArticleBodyException) {
                        LoggerUtils.logError(ex,getDatabaseSession())
                        DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                        false
                    } catch (ex: Throwable) {
                        LoggerUtils.logError(ex,getDatabaseSession())
                        false
                    }
                }
                .forEach {
                    println("Article after parsing:" + it)
                    if (it.isDownloaded()) {
                        DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().update(it) }
                    }
                }*/

        // So now here we have list of pages that need to be parsed for a certain newspaper

        do {
            val shuffledPageList = ArrayList<Page>(pageListForParsing)

            shuffledPageList.shuffle()

            println("#############################################################################################")
            println("#############################################################################################")
            println("Going to parse ${pageListForParsing.size} pages:")

            for (currentPage in shuffledPageList){

                println("Page Name: ${currentPage.name} Page Id: ${currentPage.id} ParentPage: " +
                        "${currentPage.parentPageId} Newspaper: ${currentPage.newspaper?.name}")

                val currentPageNumber: Int

                if (currentPage.isPaginated()) {
                    currentPageNumber = getLastParsedPageNumber(currentPage) + 1
                } else {
                    currentPageNumber = NOT_APPLICABLE_PAGE_NUMBER
                }

                waitForFareNetworkUsage()

                val articleList: MutableList<Article> = mutableListOf()


                val parsingResult: Pair<MutableList<Article>, String>?
                try {
                    parsingResult = PreviewPageParser.parsePreviewPageForArticles(currentPage, currentPageNumber)
                    articleList.addAll(parsingResult.first)
                } catch (e: NewsPaperNotFoundForPageException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: ParserNotFoundException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: PageLinkGenerationException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: EmptyDocumentException) {
                    e.printStackTrace()
                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: EmptyArticlePreviewException) {
                    e.printStackTrace()
                    emptyArticlePreviewPageAction(currentPage)
//                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                } catch (e: Throwable) {
                    e.printStackTrace()
                    LoggerUtils.logError(NewsServerParserException("${e.message} for page: ${currentPage.name} page no.: ${currentPageNumber} Np: ${currentPage.newspaper!!.name}"),getDatabaseSession())
//                    LoggerUtils.logError(e, getDatabaseSession())
                    continue
                }


                val parseableArticleList = mutableListOf<Article>()

                //Save all parsed article preview data
                articleList
                        .asSequence()
                        .filter {
                            getDatabaseSession().get(Article::class.java, it.id) == null
                        }
                        .forEach {
                            if (DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().save(it) }) {
                                parseableArticleList.add(it)
                            }
                        }
                //Full repeat
                //Should be active during production stage
                if (parseableArticleList.size == 0){
                    allArticleRepeatAction(currentPage,parsingResult.second)
                    continue
                }
                //save parsing details
                savePageParsingHistory(currentPage, currentPageNumber, parseableArticleList.size,parsingResult.second)

//                continue

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
                                LoggerUtils.logError(ex,getDatabaseSession())
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                                false
                            } /*catch (ex: EmptyDocumentException) {
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
                            } */catch (ex: Throwable) {
                                LoggerUtils.logError(NewsServerParserException("${ex.message} for article: ${it.articleLink}"),getDatabaseSession())
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().delete(it) }
                                false
                            }
                        }
                        .forEach {
                            println("Article after parsing:" + it)
                            if (it.isDownloaded()) {
                                DatabaseUtils.runDbTransection(getDatabaseSession()) { getDatabaseSession().update(it) }
                            }
                        }
            }
            println("#############################################################################################")
            println("#############################################################################################")
        } while (true)
    }

    private fun allArticleRepeatAction(currentPage: Page,parsingLogMessage: String) {
        savePageParsingHistory(currentPage, 0, 0,"All articles are repeated."+parsingLogMessage)
    }

    private fun savePageParsingHistory(currentPage: Page, currentPageNumber: Int, articleCount: Int,parsingLogMessage:String="") {
        DatabaseUtils.runDbTransection(getDatabaseSession()) {
            getDatabaseSession().save(PageParsingHistory(page = currentPage, pageNumber = currentPageNumber,
                                        articleCount = articleCount,parsingLogMessage = parsingLogMessage))
        }
    }

    private fun emptyArticlePreviewPageAction(currentPage: Page) {
        savePageParsingHistory(currentPage, 0, 0,"No article found.")
    }

    private fun getUnParsedArticleOfCurrentNewspaper(newspaper: Newspaper): List<Article> {

        val query = getDatabaseSession().getNamedNativeQuery(DbNamedNativeQueries.UN_PARSERD_ARTICLES_BY_NEWSPAPER_ID_NAME);

        val articleList = mutableListOf<Article>()
        query.setParameter("currentNewsPaperId", newspaper.id)
        articleList.addAll(query.resultList as MutableList<Article>)

        return articleList
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
        if (!::dbSession.isInitialized || /*!dbSession.isConnected || */!dbSession.isOpen) {
            dbSession = DbSessionManager.getNewSession()
        }
        return dbSession
    }
}
