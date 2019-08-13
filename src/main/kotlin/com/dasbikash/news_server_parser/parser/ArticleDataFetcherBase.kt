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
import kotlin.random.Random

abstract class ArticleDataFetcherBase constructor(opMode: ParserMode) : Thread() {

    private val isSelfParser: Boolean

    init {
        isSelfParser = ((opMode == ParserMode.GET_SYNCED) || (opMode == ParserMode.RUNNING))
    }

    private var lastNetworkRequestTSMap = mutableMapOf<String, Long>()
    private val MIN_START_DELAY = 1 * 1000L //1 secs
    private val MIN_DELAY_BETWEEN_NETWORK_REQUESTS = 5 * 1000L //5 secs
    protected val PAGE_NUMBER_NOT_APPLICABLE = 0
    lateinit var dbSession: Session

    private val DELEY_BETWEEN_ITERATION = 60 * 1000L //60 secs

    override fun run() {
        LoggerUtils.logOnDb("Starting ${this::class.java.simpleName}", getDatabaseSession())

        sleep(MIN_START_DELAY + Random(System.currentTimeMillis()).nextLong(MIN_START_DELAY))

        val newsPapers = getReleventNewsPapers().toMutableList()

        do {
            val pageListForParsing = mutableListOf<Page>()

            newsPapers.asSequence().forEach {
                it.pageList?.filter { it.hasData() }?.forEach { pageListForParsing.add(it) }
            }

            pageListForParsing.shuffled().asSequence().forEach {

                getDatabaseSession().refresh(it.newspaper)

                try {
                    doParsingForPage(it)

                    //Mark pages with articles as active
                    if (!it.active) {
                        if (DatabaseUtils.getArticleCountForPage(getDatabaseSession(), it.id) > 0) {
                            it.active = true
                            DatabaseUtils.runDbTransection(getDatabaseSession()) {
                                getDatabaseSession().update(it)
                            }
                        }
                    }

                    //Recalculate Page parsing intervals if necessary

                    var pageParsingInterval = DatabaseUtils.getPageParsingIntervalForPage(getDatabaseSession(), it)

                    if (pageParsingInterval == null) {
                        pageParsingInterval = PageParsingInterval.recalculate(getDatabaseSession(), it)
                        DatabaseUtils.runDbTransection(getDatabaseSession()) {
                            getDatabaseSession().save(pageParsingInterval)
                            LoggerUtils.logOnConsole("PageParsingInterval set to: ${pageParsingInterval.parsingIntervalMS} " +
                                    "for page ${it.name} of NP: ${it.newspaper?.name}")
                        }
                    } else if (pageParsingInterval.needRecalculation(getDatabaseSession())) {
                        val newInterval = PageParsingInterval.recalculate(getDatabaseSession(), it)
                        if (pageParsingInterval.parsingIntervalMS == newInterval.parsingIntervalMS) {
                            newInterval.parsingIntervalMS = newInterval.parsingIntervalMS!! + 1
                        }
                        pageParsingInterval.parsingIntervalMS = newInterval.parsingIntervalMS!!
                        DatabaseUtils.runDbTransection(getDatabaseSession()) {
                            getDatabaseSession().update(pageParsingInterval)
                            LoggerUtils.logOnConsole("PageParsingInterval updated to: ${pageParsingInterval.parsingIntervalMS} " +
                                    "for page ${it.name} of NP: ${it.newspaper?.name}")
                        }
                    }

                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                    LoggerUtils.logOnDb("Exiting ${this::class.java.simpleName}", getDatabaseSession())
                    getDatabaseSession().close()
                    return
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                    LoggerUtils.logError(ex, getDatabaseSession())
                }
            }
            sleep(DELEY_BETWEEN_ITERATION)
            newsPapers.clear()
            getDatabaseSession().close()
            newsPapers.addAll(getReleventNewsPapers())
        } while (newsPapers.isNotEmpty())
    }

    abstract fun doParsingForPage(currentPage: Page)

    private fun getReleventNewsPapers() =
            DatabaseUtils.getAllNewspapers(getDatabaseSession())
                    .filter {
                        if (isSelfParser) {
                            val opMode = it.getOpMode(getDatabaseSession())
                            return@filter ((opMode == ParserMode.RUNNING) || (opMode == ParserMode.GET_SYNCED))
                        } else {
                            return@filter it.getOpMode(getDatabaseSession()) == ParserMode.PARSE_THROUGH_CLIENT
                        }
                    }

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
                DatabaseUtils.getLatestPageParsingHistoryForPage(getDatabaseSession(), currentPage)
        if (pageParsingHistory == null) {
            return true
        }
        DatabaseUtils.runDbTransection(getDatabaseSession()) {
            getDatabaseSession().refresh(pageParsingHistory)
        }
        return (System.currentTimeMillis() - pageParsingHistory.created!!.time) > pageParsingInterval.parsingIntervalMS!!
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

    protected fun waitForFareNetworkUsage(page: Page) {

        val lastNetworkRequestTS = lastNetworkRequestTSMap.get(page.newspaper!!.id) ?: 0L

        val ramdomDelay = Random(System.currentTimeMillis()).nextLong(MIN_DELAY_BETWEEN_NETWORK_REQUESTS)

        var delayPeriod = MIN_DELAY_BETWEEN_NETWORK_REQUESTS - (System.currentTimeMillis() - lastNetworkRequestTS)

        if (delayPeriod > 0) {
            delayPeriod += ramdomDelay
        } else {
            delayPeriod = ramdomDelay
        }

        sleep(delayPeriod)
        lastNetworkRequestTSMap.put(page.newspaper!!.id, System.currentTimeMillis())
    }

    protected fun getLastParsedPageNumber(page: Page): Int {
        DatabaseUtils.getLatestPageParsingHistoryForPage(getDatabaseSession(), page)?.let {
            return it.pageNumber
        }
        return 0

    }

    protected fun getDatabaseSession(): Session {
        if (!::dbSession.isInitialized || !dbSession.isOpen || !dbSession.isConnected) {
            if (::dbSession.isInitialized) {
                dbSession.close()
            }
            dbSession = DbSessionManager.getNewSession()
        }
        return dbSession
    }
}