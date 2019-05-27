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
import com.dasbikash.news_server_parser.exceptions.HighestLevelException
import com.dasbikash.news_server_parser.exceptions.ParserRestartedException
import com.dasbikash.news_server_parser.exceptions.ParserStoppedException
import com.dasbikash.news_server_parser.exceptions.ReportGenerationException
import com.dasbikash.news_server_parser.exceptions.handler.ParserExceptionHandler
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.model.ParserMode
import com.dasbikash.news_server_parser.parser.ArticleDataFetcherForNewsPaper
import com.dasbikash.news_server_parser.utils.DateUtils
import com.dasbikash.news_server_parser.utils.ReportGenerationUtils
import org.hibernate.Session
import java.util.*


object DataParsingCoordinator {

    private val ITERATION_DELAY = 15 * 60 * 1000L //15 mins

    private val articleDataFetcherMap
            = mutableMapOf<String, ArticleDataFetcherForNewsPaper>()

    private lateinit var currentDate:Calendar


    @JvmStatic
    fun main(args: Array<String>) {
        currentDate = Calendar.getInstance()
        do {
            try {
                val session = DbSessionManager.getNewSession()

                val allNewspapers = DatabaseUtils.getAllNewspapers(session)

                allNewspapers.asSequence().forEach {
                    if (it.active){
                        if (articleDataFetcherMap.containsKey(it.id) && !articleDataFetcherMap.get(it.id)!!.isAlive) {
                            articleDataFetcherMap.remove(it.id)
                            ParserExceptionHandler.handleException(ParserRestartedException(it))
                        }
                        val currentOpMode = getOpModeForNewsPaper(session,it)
                        if (!articleDataFetcherMap.containsKey(it.id)) {
                            startArticleDataFetcherForNewspaper(session, it,currentOpMode)
                        }else{
                            if (currentOpMode != articleDataFetcherMap.get(it.id)!!.opMode){
                                stopParserForNewspaper(it)
                                startArticleDataFetcherForNewspaper(session, it,currentOpMode)
                            }
                        }
                    }else{
                        if (articleDataFetcherMap.containsKey(it.id)){
                            stopParserForNewspaper(it)
                        }
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
                Thread.sleep(ITERATION_DELAY)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
                handleException(ex)
            }
        } while (true)
    }

    private fun generateAndDistributeDailyReport(today: Date,session: Session) {
        ReportGenerationUtils.prepareDailyReport(today, session)
        ReportGenerationUtils.emailDailyReport(today)
    }

    private fun generateAndDistributeWeeklyReport(today: Date,session: Session) {
        ReportGenerationUtils.prepareWeeklyReport(today, session)
        ReportGenerationUtils.emailWeeklyReport(today)
    }

    private fun generateAndDistributeMonthlyReport(today: Date,session: Session) {
        ReportGenerationUtils.prepareMonthlyReport(today, session)
        ReportGenerationUtils.emailMonthlyReport(today)
    }

    private fun stopParserForNewspaper(newspaper: Newspaper) {
        articleDataFetcherMap.get(newspaper.id)!!.interrupt()
        articleDataFetcherMap.remove(newspaper.id)
        ParserExceptionHandler.handleException(ParserStoppedException(newspaper))
    }

    private fun startArticleDataFetcherForNewspaper(session: Session, it: Newspaper,currentOpMode:ParserMode) {
        val articleDataFetcherForNewsPaper = ArticleDataFetcherForNewsPaper(it, currentOpMode)
        articleDataFetcherMap.put(it.id, articleDataFetcherForNewsPaper)
        session.detach(it)
        articleDataFetcherForNewsPaper.start()
    }

    private fun getOpModeForNewsPaper(session: Session,newspaper: Newspaper): ParserMode {
        val newspaperOpModeEntry = DatabaseUtils.getNewspaperOpModeEntry(session, newspaper)!!
        println(newspaperOpModeEntry)
        return newspaperOpModeEntry.opMode!!
    }

    private fun handleException(ex: InterruptedException) {
        ParserExceptionHandler.handleException(HighestLevelException(ex))
    }
}
