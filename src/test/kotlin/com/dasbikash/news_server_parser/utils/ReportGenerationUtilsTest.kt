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

package com.dasbikash.news_server_parser.utils

import com.dasbikash.news_server_parser.database.DbSessionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class ReportGenerationUtilsTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun prepareDailyReportTest(){
        val session = DbSessionManager.getNewSession()
        val today = Date()
        ReportGenerationUtils.prepareDailyReport(FileUtils.getDailyReportFilePath(today),today, session)
    }

    @Test
    fun prepareWeeklyReportTest(){
        val session = DbSessionManager.getNewSession()
        val today = Date()
        ReportGenerationUtils.prepareWeeklyReport(FileUtils.getWeeklyReportFilePath(today),today, session)
    }

    @Test
    fun prepareMonthlyReportTest(){
        val session = DbSessionManager.getNewSession()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH,Calendar.JUNE)
        calendar.set(Calendar.DAY_OF_MONTH,15)
        val today = calendar.time
        ReportGenerationUtils.prepareMonthlyReport(FileUtils.getMonthlyReportFilePath(today),today, session)
    }
}