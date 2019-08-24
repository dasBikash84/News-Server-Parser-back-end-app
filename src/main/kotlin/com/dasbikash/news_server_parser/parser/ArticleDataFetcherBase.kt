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
import com.dasbikash.news_server_parser.utils.RxJavaUtils
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

    private val DELEY_BETWEEN_PARSER_LAUNCH_RETRY = 1000L //1 secs
    private val ACTIVE_PARSER_COUNT = 2

    private val activeNewsPapers = mutableListOf<Newspaper>()

    override fun run() {
        LoggerUtils.logOnDb("Starting ${this::class.java.simpleName}", getDatabaseSession())
        sleep(MIN_START_DELAY + Random(System.currentTimeMillis()).nextLong(MIN_START_DELAY))

        do {
            DatabaseUtils.getAllPages(getDatabaseSession())
                    .filter { it.isTopLevelPage() && !it.active }
                    .forEach {
                        it.active = true
                        DatabaseUtils.runDbTransection(getDatabaseSession()) {
                            getDatabaseSession().update(it)
                        }
                    }

            val pageListForParsing = mutableListOf<Page>()
            val newspapers = getReleventNewsPapers(getDatabaseSession())
            newspapers
                    .flatMap {it.pageList?.filter { it.hasData() } ?: emptyList()}
                    .forEach { pageListForParsing.add(it) }

            while (pageListForParsing.isNotEmpty()){
                val currentPage = pageListForParsing.shuffled().get(0)
                if(!launchParserForPage(currentPage,getDatabaseSession())){
                    sleep(DELEY_BETWEEN_PARSER_LAUNCH_RETRY)
                }else{
                    pageListForParsing.remove(currentPage)
                }
            }
            getDatabaseSession().close()
        } while (newspapers.isNotEmpty())
    }

    private fun launchParserForPage(page:Page,session: Session):Boolean{
        synchronized(activeNewsPapers){
            if (activeNewsPapers.size >= ACTIVE_PARSER_COUNT ||
                    activeNewsPapers.count { it.id == page.newspaper!!.id }>0){
                return false
            }else{
                activeNewsPapers.add(page.newspaper!!)
                session.detach(page)
                RxJavaUtils.doTaskInBackGround {
                    parsePage(page,{it -> removeNewsPaperForActiveList(it)})
                }
                return true
            }
        }
    }

    fun removeNewsPaperForActiveList(newpaperId:String){
        synchronized(activeNewsPapers){
            var newspaper:Newspaper? = null
            activeNewsPapers.asSequence().forEach {
                if (it.id == newpaperId){
                    newspaper = it
                    return@forEach
                }
            }
            newspaper?.let {
                activeNewsPapers.remove(it)
            }
        }
    }

    private fun parsePage(page: Page, removeNewsPaperForActiveList: (newspaperId:String)->Unit) {

        val session = DbSessionManager.getNewSession()
        session.saveOrUpdate(page)

        try {
            doParsingForPage(page,session)

            //Mark pages with articles or if top-level, as active
            if (!page.active) {
                if (page.isTopLevelPage() ||
                        DatabaseUtils.getArticleCountForPage(session, page.id) > 0) {
                    page.active = true
                    DatabaseUtils.runDbTransection(session) {
                        session.update(page)
                    }
                }
            }

            //Recalculate Page parsing intervals if necessary
            var pageParsingInterval = DatabaseUtils.getPageParsingIntervalForPage(session, page)

            if (pageParsingInterval == null) {
                pageParsingInterval = PageParsingInterval.recalculate(session, page)
                DatabaseUtils.runDbTransection(session) {
                    session.save(pageParsingInterval)
                    LoggerUtils.logOnConsole("PageParsingInterval set to: ${pageParsingInterval.parsingIntervalMS} " +
                            "for page ${page.name} of NP: ${page.newspaper?.name}")
                }
            } else if (pageParsingInterval.needRecalculation(session)) {
                val newInterval = PageParsingInterval.recalculate(session, page)
                if (pageParsingInterval.parsingIntervalMS == newInterval.parsingIntervalMS) {
                    newInterval.parsingIntervalMS = newInterval.parsingIntervalMS!! + 1
                }
                pageParsingInterval.parsingIntervalMS = newInterval.parsingIntervalMS!!
                DatabaseUtils.runDbTransection(session) {
                    session.update(pageParsingInterval)
                    LoggerUtils.logOnConsole("PageParsingInterval updated to: ${pageParsingInterval.parsingIntervalMS!! / 1000 / 60} mins " +
                            "for page ${page.name} of NP: ${page.newspaper?.name}")
                }
            }

        } catch (ex: InterruptedException) {
            ex.printStackTrace()
            LoggerUtils.logOnDb("Exiting ${this::class.java.simpleName}", session)
            session.close()
        } catch (ex: Throwable) {
            ex.printStackTrace()
            LoggerUtils.logError(ex, session)
        }

        removeNewsPaperForActiveList(page.newspaper!!.id)
        session.close()
    }

    abstract fun doParsingForPage(currentPage: Page,session: Session)

    private fun getReleventNewsPapers(session: Session) =
            DatabaseUtils.getAllNewspapers(session)
                    .filter {
                        if (isSelfParser) {
                            val opMode = it.getOpMode(session)
                            return@filter ((opMode == ParserMode.RUNNING) || (opMode == ParserMode.GET_SYNCED))
                        } else {
                            return@filter it.getOpMode(session) == ParserMode.PARSE_THROUGH_CLIENT
                        }
                    }

    protected fun goForPageParsing(currentPage: Page,session: Session): Boolean {
        val pageParsingInterval =
                DatabaseUtils.getPageParsingIntervalForPage(session, currentPage)
        if (pageParsingInterval == null) {
            return true
        }
        val pageParsingHistory =
                DatabaseUtils.getLatestPageParsingHistoryForPage(session, currentPage)
        if (pageParsingHistory == null) {
            return true
        }
        return (System.currentTimeMillis() - pageParsingHistory.created!!.time) > pageParsingInterval.parsingIntervalMS!!
    }

    protected fun emptyPageAction(currentPage: Page, parsingLogMessage: String, resetOnStart: Boolean = false,session: Session) {
        savePageParsingHistory(currentPage, 0, 0,
                "No article found." + parsingLogMessage, resetOnStart,session)
    }

    protected fun allArticleRepeatAction(currentPage: Page, parsingLogMessage: String,session: Session) {
        savePageParsingHistory(currentPage, 0, 0, "All articles are repeated." + parsingLogMessage,session = session)
    }

    protected fun savePageParsingHistory(currentPage: Page, currentPageNumber: Int, articleCount: Int
                                         , parsingLogMessage: String = "", resetOnStart: Boolean = false,session: Session)
    {
        if (!resetOnStart) {
            if (articleCount > 0) {
                LoggerUtils.logOnConsole("${articleCount} new article found for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
            } else {
                LoggerUtils.logOnConsole("No new article found for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
            }
        } else {
            LoggerUtils.logOnConsole("Parser reset on start for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
        }
        DatabaseUtils.runDbTransection(session) {
            session.save(PageParsingHistory(page = currentPage, pageNumber = currentPageNumber,
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

    protected fun getLastParsedPageNumber(page: Page,session: Session): Int {
        DatabaseUtils.getLatestPageParsingHistoryForPage(session, page)?.let {
            return it.pageNumber
        }
        return 0

    }

    private fun getDatabaseSession(): Session {
        if (!::dbSession.isInitialized || !dbSession.isOpen || !dbSession.isConnected) {
            if (::dbSession.isInitialized) {
                dbSession.close()
            }
            dbSession = DbSessionManager.getNewSession()
        }
        return dbSession
    }
}