@file:JvmName("com.dasbikash.news_server_parser.DataParsingCoordinator")

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


package com.dasbikash.news_server_parser

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.exceptions.ReportGenerationException
import com.dasbikash.news_server_parser.exceptions.generic.HighestLevelException
import com.dasbikash.news_server_parser.exceptions.handler.ParserExceptionHandler
import com.dasbikash.news_server_parser.firebase.RealTimeDbAdminTaskUtils
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.model.ParserMode
import com.dasbikash.news_server_parser.parser.ArticleDataFetcherForPageSelf
import com.dasbikash.news_server_parser.parser.ArticleDataFetcherForPageThroughClient
import com.dasbikash.news_server_parser.utils.DateUtils
import com.dasbikash.news_server_parser.utils.ReportGenerationUtils
import org.hibernate.Session
import java.util.*


object DataParsingCoordinator {

    private val ITERATION_DELAY = 15 * 60 * 1000L //15 mins

    private var articleDataFetcherForPageSelf:ArticleDataFetcherForPageSelf?=null
    private var articleDataFetcherForPageThroughClient:ArticleDataFetcherForPageThroughClient?=null

    private lateinit var currentDate:Calendar


    @JvmStatic
    fun main(args: Array<String>) {
        currentDate = Calendar.getInstance()
        do {
            try {
                val session = DbSessionManager.getNewSession()

                if (articleDataFetcherForPageSelf == null){
                    if ((getNpCountWithRunningOpMode(session)+ getNpCountWithGetSyncedOpMode(session)) > 0){
                        articleDataFetcherForPageSelf = ArticleDataFetcherForPageSelf()
                        articleDataFetcherForPageSelf!!.start()
                    }
                }else{
                    if ((getNpCountWithRunningOpMode(session)+ getNpCountWithGetSyncedOpMode(session)) == 0){
                        articleDataFetcherForPageSelf!!.interrupt()
                        articleDataFetcherForPageSelf = null
                    }else if (!articleDataFetcherForPageSelf!!.isAlive){
                        articleDataFetcherForPageSelf = ArticleDataFetcherForPageSelf()
                        articleDataFetcherForPageSelf!!.start()
                    }
                }

                if (articleDataFetcherForPageThroughClient == null){
                    if ((getNpCountWithParseThroughClientOpMode(session)) > 0){
                        articleDataFetcherForPageThroughClient = ArticleDataFetcherForPageThroughClient()
                        articleDataFetcherForPageThroughClient!!.start()
                    }
                }else{
                    if ((getNpCountWithParseThroughClientOpMode(session)) == 0){
                        articleDataFetcherForPageThroughClient!!.interrupt()
                        articleDataFetcherForPageThroughClient = null
                    }else if (!articleDataFetcherForPageThroughClient!!.isAlive){
                        articleDataFetcherForPageThroughClient = ArticleDataFetcherForPageThroughClient()
                        articleDataFetcherForPageThroughClient!!.start()
                    }
                }

                val now = Calendar.getInstance()
                if (now.get(Calendar.YEAR)> currentDate.get(Calendar.YEAR) ||
                        now.get(Calendar.DAY_OF_YEAR)> currentDate.get(Calendar.DAY_OF_YEAR)){
                    try {

                        generateAndDistributeDailyReport(now.time!!, session)

                        if (DateUtils.isFirstDayOfWeek(now.time)) {
                            generateAndDistributeWeeklyReport(now.time, session)
                        }

                        if (DateUtils.isFirstDayOfMonth(now.time)) {
                            generateAndDistributeMonthlyReport(now.time, session)
                        }

                        currentDate = now
                    }catch (ex:Throwable){
                        ex.printStackTrace()
                        ParserExceptionHandler.handleException(ReportGenerationException(ex))
                    }
                }

                session.close()
                RealTimeDbAdminTaskUtils.init()
                Thread.sleep(ITERATION_DELAY)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
                handleException(ex)
            }
        } while (true)
    }

    private fun generateAndDistributeDailyReport(today: Date,session: Session) {
        println("Starting daily article parsing report generation.")
        ReportGenerationUtils.prepareDailyReport(today, session)
        println("Daily article parsing report generated.")
        ReportGenerationUtils.emailDailyReport(today)
        println("Daily article parsing report distributed.")
    }

    private fun generateAndDistributeWeeklyReport(today: Date,session: Session) {
        println("Starting weekly article parsing report generation.")
        ReportGenerationUtils.prepareWeeklyReport(today, session)
        println("Weekly article parsing report generated.")
        ReportGenerationUtils.emailWeeklyReport(today)
        println("Weekly article parsing report distributed.")
    }

    private fun generateAndDistributeMonthlyReport(today: Date,session: Session) {
        println("Starting monthly article parsing report generation.")
        ReportGenerationUtils.prepareMonthlyReport(today, session)
        println("Monthly article parsing report generated.")
        ReportGenerationUtils.emailMonthlyReport(today)
        println("Monthly article parsing report distributed.")
    }

    private fun getAllActiveNps(session: Session):List<Newspaper> =
            DatabaseUtils.getAllNewspapers(session).filter { it.active }

    private fun getNpCountWithRunningOpMode(session: Session)=
        getAllActiveNps(session).map { DatabaseUtils.getOpModeForNewsPaper(session,it) }.count { it==ParserMode.RUNNING }

    private fun getNpCountWithGetSyncedOpMode(session: Session)=
            getAllActiveNps(session).map { DatabaseUtils.getOpModeForNewsPaper(session,it) }.count { it==ParserMode.GET_SYNCED }

    private fun getNpCountWithParseThroughClientOpMode(session: Session)=
            getAllActiveNps(session).map { DatabaseUtils.getOpModeForNewsPaper(session,it) }.count { it==ParserMode.PARSE_THROUGH_CLIENT }

    private fun handleException(ex: InterruptedException) {
        ParserExceptionHandler.handleException(HighestLevelException(ex))
    }
}
