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

package com.dasbikash.news_server_parser.database

import com.dasbikash.news_server_parser.utils.DateUtils
import com.dasbikash.news_server_parser.utils.FileUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

internal class DatabaseUtilsTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

//    @Test
//    fun readArticleCountForPage(){
//        println(DatabaseUtils.getArticleCountForPage(DbSessionManager.getNewSession(),"PAGE_ID_619"))
//    }

    //    @Test
//    fun readArticleCountForPage(){
//        val session = DbSessionManager.getNewSession()
//
//        println(DatabaseUtils.getArticleCountForPage(session,"PAGE_ID_1"))
//
//        session.close()
//    }
//    @Test
//    fun getNewspaperOpModeEntryTest() {
//        val session = DbSessionManager.getNewSession()
//
//        DatabaseUtils.getAllNewspapers(session).asSequence().forEach {
//            DatabaseUtils.getNewspaperOpModeEntry(session,it)?.let {
//                println(it)
//            }
//        }
//    }
    @Test
    fun testGetArticleCountForPageOfDay(){
        val session = DbSessionManager.getNewSession()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH,Calendar.JUNE)
        calendar.set(Calendar.DAY_OF_MONTH,1)
        DatabaseUtils.getAllPages(session).get(155).apply {
            println(this)
            println(DatabaseUtils.getArticleCountForPage(session,this.id))
            println(DatabaseUtils.getArticleCountForPageOfYesterday(session,this,calendar.time))
            println(DatabaseUtils.getArticleCountForPageOfLastWeek(session,this,calendar.time))
            println(DatabaseUtils.getArticleCountForPageOfLastMonth(session,this,calendar.time))
            println(DateUtils.isFirstDayOfWeek(calendar.time))
            println(DateUtils.isFirstDayOfMonth(calendar.time))
            File(FileUtils.getDailyReportFilePath(calendar.time)).appendText("\n"+FileUtils.getDailyReportFilePath(calendar.time))
            File(FileUtils.getWeeklyReportFilePath(calendar.time)).appendText("\n"+FileUtils.getWeeklyReportFilePath(calendar.time))
            File(FileUtils.getMonthlyReportFilePath(calendar.time)).appendText("\n"+FileUtils.getMonthlyReportFilePath(calendar.time))
        }
    }

}