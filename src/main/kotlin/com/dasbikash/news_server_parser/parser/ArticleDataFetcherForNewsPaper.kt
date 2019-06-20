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
import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.utils.LoggerUtils
import org.hibernate.Session
import java.util.*
import kotlin.random.Random

abstract class ArticleDataFetcherForNewsPaper(
        val newspaper: Newspaper, //caller must end session before sending Newspaper object
        val opMode: ParserMode
) : Thread() {

    private var lastNetworkRequestTS = 0L
    private val MIN_DELAY_BETWEEN_NETWORK_REQUESTS = 10 * 1000L //10 secs
    protected val PAGE_NUMBER_NOT_APPLICABLE = 0

    private val topLevelPages = mutableListOf<Page>()
    private val childPageMap = mutableMapOf<Page, ArrayList<Page>>()
    lateinit var dbSession: Session

    private val DELEY_BETWEEN_ITERATION = 1 * 60 * 1000L //5 mins

    override fun run() {
        sleep(Random(System.currentTimeMillis()).nextLong(MIN_DELAY_BETWEEN_NETWORK_REQUESTS))
        LoggerUtils.logOnConsole("Parser for ${newspaper.name} started.")
        LoggerUtils.logOnDb("Parser for ${newspaper.name} started", getDatabaseSession())

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

        do {

            LoggerUtils.logOnConsole("Going to parse ${pageListForParsing.size} pages for Np: ${newspaper.name}")

            try {

                doParsingForPages(pageListForParsing.toList())

                try {
                    sleep(DELEY_BETWEEN_ITERATION)
                } catch (ex: InterruptedException) {
                    throw ex
                }

            } catch (ex: InterruptedException) {
                ex.printStackTrace()
                LoggerUtils.logOnConsole("Exiting ${this::class.java.simpleName} for ${newspaper.name}")
                return
            }
            //Mark pages with articles as active
            pageListForParsing.asSequence()
                    .forEach {
                        if (!it.active) {
                            if (!it.isTopLevelPage()) {
                                it.active = DatabaseUtils.getArticleCountForPage(getDatabaseSession(), it.id) > 0
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
            //Recalculate Page parsing intervals if necessary
            pageListForParsing.asSequence()
                    .forEach {
                        var pageParsingInterval = DatabaseUtils.getPageParsingIntervalForPage(getDatabaseSession(), it)
                        if (pageParsingInterval == null) {
                            pageParsingInterval = PageParsingInterval.recalculate(it)
                            DatabaseUtils.runDbTransection(getDatabaseSession()) {
                                getDatabaseSession().save(pageParsingInterval)
                                LoggerUtils.logOnConsole("PageParsingInterval set to: ${pageParsingInterval.parsingIntervalMS} " +
                                        "for page ${it.name} of NP: ${it.newspaper?.name}")
                            }
                        } else if (pageParsingInterval.needRecalculation()) {
                            val newInterval = PageParsingInterval.recalculate(it)
                            if(pageParsingInterval.parsingIntervalMS == newInterval.parsingIntervalMS){
                                newInterval.parsingIntervalMS = newInterval.parsingIntervalMS!!+1
                            }
                            pageParsingInterval.parsingIntervalMS = newInterval.parsingIntervalMS!!
                            DatabaseUtils.runDbTransection(getDatabaseSession()) {
                                getDatabaseSession().update(pageParsingInterval)
                                LoggerUtils.logOnConsole("PageParsingInterval updated to: ${pageParsingInterval.parsingIntervalMS} " +
                                        "for page ${it.name} of NP: ${it.newspaper?.name}")
                            }
                        }
                    }
        } while (pageListForParsing.size > 0)
    }

    abstract protected fun doParsingForPages(pageListForParsing: List<Page>)

    protected fun goForPageParsing(currentPage: Page): Boolean {

        val pageParsingInterval =
                DatabaseUtils.getPageParsingIntervalForPage(getDatabaseSession(), currentPage)

        if (pageParsingInterval == null) {
            return true
        }

        DatabaseUtils.runDbTransection(getDatabaseSession()) {
            getDatabaseSession().refresh(pageParsingInterval)
        }

        val pageParsingHistory =
                DatabaseUtils.getLatestPageParsingHistoryForPage(getDatabaseSession(), pageParsingInterval.page!!)

        if (pageParsingHistory == null) {
            return true
        }
        DatabaseUtils.runDbTransection(getDatabaseSession()) {
            getDatabaseSession().refresh(pageParsingHistory)
        }
        return (System.currentTimeMillis() - pageParsingHistory.created!!.time).toInt() > pageParsingInterval.parsingIntervalMS!!
    }

    protected fun emptyPageAction(currentPage: Page, parsingLogMessage: String, resetOnStart: Boolean = false) {
        savePageParsingHistory(currentPage, 0, 0,
                "No article found." + parsingLogMessage, resetOnStart)
    }

    protected fun allArticleRepeatAction(currentPage: Page, parsingLogMessage: String) {
        savePageParsingHistory(currentPage, 0, 0, "All articles are repeated." + parsingLogMessage)
    }

    protected fun savePageParsingHistory(currentPage: Page, currentPageNumber: Int, articleCount: Int
                                       , parsingLogMessage: String = "", resetOnStart: Boolean = false) {
        if (!resetOnStart) {
            if (articleCount > 0) {
                LoggerUtils.logOnConsole("${articleCount} new article found for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
            } else {
                LoggerUtils.logOnConsole("No new article found for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
            }
        } else {
            LoggerUtils.logOnConsole("Parser reset on start for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
        }
        DatabaseUtils.runDbTransection(getDatabaseSession()) {
            getDatabaseSession().save(PageParsingHistory(page = currentPage, pageNumber = currentPageNumber,
                    articleCount = articleCount, parsingLogMessage = parsingLogMessage))
        }
    }

    protected fun waitForFareNetworkUsage() {

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

    protected fun getLastParsedPageNumber(page: Page): Int {
        DatabaseUtils.getLatestPageParsingHistoryForPage(getDatabaseSession(), page)?.let {
            return it.pageNumber
        }
        return 0

    }

    protected fun getDatabaseSession(): Session {
        if (!::dbSession.isInitialized || !dbSession.isOpen) {
            dbSession = DbSessionManager.getNewSession()
        }
        return dbSession
    }
}