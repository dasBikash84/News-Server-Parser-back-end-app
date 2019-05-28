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

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.model.Page
import org.hibernate.Session
import java.io.File
import java.util.*

object ReportGenerationUtils {

    fun getTableHeader(): String {
        return "Sl,Page Name,Weekly,Page Id,Parent Page Id,Newspaper,Article Download Count"
    }

    fun prepareDailyReport(today: Date, session: Session) {

        val reportFilePath = FileUtils.getDailyReportFilePath(today)
        if (File(reportFilePath).exists()) {
            File(reportFilePath).delete()
        }
        val reportFile = File(reportFilePath)
        val yesterDay = DateUtils.getYesterDay(today)

        reportFile.appendText("Article parsing report of: ${DateUtils.getDateStringForDb(yesterDay)}\n\n")

        val pages = DatabaseUtils.getAllPages(session)
        val pageArticleCountMap = mutableMapOf<Page,Int>()
        pages.asSequence().filter { it.hasData() }.sortedBy { it.newspaper!!.name!! }.forEach {
            val articleCountOfYesterday = DatabaseUtils.getArticleCountForPageOfYesterday(session, it, today)
            pageArticleCountMap.put(it,articleCountOfYesterday)
        }
        addReportDataToFile(reportFile, pageArticleCountMap)
//        addFromBeginningReport(reportFile, pages, session)
    }

    fun prepareWeeklyReport(today: Date, session: Session) {

        val reportFilePath = FileUtils.getWeeklyReportFilePath(today)

        if (File(reportFilePath).exists()) {
            File(reportFilePath).delete()
        }
        val reportFile = File(reportFilePath)

        val lastWeekFirstDay = DateUtils.getLastWeekSameDay(today)
        val lastWeekLastDay = DateUtils.getYesterDay(today)

        reportFile.appendText("Article parsing report of week: ${DateUtils.getDateStringForDb(lastWeekFirstDay)} to "+
                                "${DateUtils.getDateStringForDb(lastWeekLastDay)}\n\n")

        val pages = DatabaseUtils.getAllPages(session)
        val pageArticleCountMap = mutableMapOf<Page,Int>()
        pages.asSequence().filter { it.hasData() }.sortedBy { it.newspaper!!.name!! }.forEach {
            val articleCountOfLastWeek = DatabaseUtils.getArticleCountForPageOfLastWeek(session, it, today)
            pageArticleCountMap.put(it,articleCountOfLastWeek)
        }

        addReportDataToFile(reportFile, pageArticleCountMap)
//        addFromBeginningReport(reportFile, pages, session)
    }

    fun prepareMonthlyReport(today: Date, session: Session) {

        val reportFilePath = FileUtils.getMonthlyReportFilePath(today)

        if (File(reportFilePath).exists()) {
            File(reportFilePath).delete()
        }
        val reportFile = File(reportFilePath)
        val firstDayOfLastMonth = DateUtils.getFirstDayOfLastMonth(today)

        reportFile.appendText("Article parsing report of: ${DateUtils.getYearMonthStr(firstDayOfLastMonth)}\n\n")

        val pages = DatabaseUtils.getAllPages(session)
        val pageArticleCountMap = mutableMapOf<Page,Int>()

        pages.asSequence().filter { it.hasData() }.sortedBy { it.newspaper!!.name!! }.forEach {
            val articleCountOfLastMonth = DatabaseUtils.getArticleCountForPageOfLastMonth(session, it, today)
            pageArticleCountMap.put(it,articleCountOfLastMonth)
        }

        addReportDataToFile(reportFile, pageArticleCountMap)
        addFromBeginningReport(reportFile, pages, session)
    }

    private fun addReportDataToFile(reportFile: File, pageArticleCountMap: MutableMap<Page, Int>) {
        reportFile.appendText("For pages with articles:\n")
        reportFile.appendText("${getTableHeader()}\n")

        var sln = 0
        var totalArticleCountOfLastMonth = 0

        pageArticleCountMap.keys.asSequence().filter { pageArticleCountMap.get(it) != 0 }.forEach {
            val articleCountOfLastMonth = pageArticleCountMap.get(it)!!
            totalArticleCountOfLastMonth += articleCountOfLastMonth
            reportFile.appendText("${++sln},${it.name},${getIsWeeklyText(it)},${it.id},${getParentPageIdText(it)},${it.newspaper!!.name},${articleCountOfLastMonth}\n")
        }
        reportFile.appendText(",,,,,Total,${totalArticleCountOfLastMonth}\n\n")

        reportFile.appendText("For pages without any article:\n")
        reportFile.appendText("${getTableHeader()}\n")
        sln = 0

        pageArticleCountMap.keys.asSequence().filter { pageArticleCountMap.get(it) == 0 }.forEach {
            val articleCountOfLastMonth = pageArticleCountMap.get(it)!!
            reportFile.appendText("${++sln},${it.name},${getIsWeeklyText(it)},${it.id},${getParentPageIdText(it)},${it.newspaper!!.name},${articleCountOfLastMonth}\n")
        }
    }



    fun emailDailyReport(today: Date) {
        val reportFilePath = FileUtils.getDailyReportFilePath(today)
        val yesterDay = DateUtils.getYesterDay(today)

        EmailUtils.sendEmail("Daily parsing report",
                                "Parsing report of ${DateUtils.getDateStringForDb(yesterDay)}",
                                        reportFilePath)
    }

    fun emailWeeklyReport(today: Date) {
        val reportFilePath = FileUtils.getWeeklyReportFilePath(today)
        val lastWeekFirstDay = DateUtils.getLastWeekSameDay(today)
        val lastWeekLastDay = DateUtils.getYesterDay(today)

        EmailUtils.sendEmail("Weekly parsing report",
                                "Parsing report of ${DateUtils.getDateStringForDb(lastWeekFirstDay)} to ${DateUtils.getDateStringForDb(lastWeekLastDay)}",
                                        reportFilePath)
    }

    fun emailMonthlyReport(today: Date) {
        val reportFilePath = FileUtils.getMonthlyReportFilePath(today)
        val firstDayOfLastMonth = DateUtils.getFirstDayOfLastMonth(today)

        EmailUtils.sendEmail("Monthly parsing report",
                                "Parsing report of ${DateUtils.getYearMonthStr(firstDayOfLastMonth)}",
                                    reportFilePath)
    }

    private fun addFromBeginningReport(reportFile: File, pages: List<Page>, session: Session) {
        reportFile.appendText("\n\nParsing report from Beginning:\n\n")

        val pageArticleCountMap = mutableMapOf<Page,Int>()
        pages.asSequence().filter { it.hasData() }.sortedBy { it.newspaper!!.name!! }.forEach {
            val articleCountFromBeginning = DatabaseUtils.getArticleCountForPage(session, it.id)
            pageArticleCountMap.put(it,articleCountFromBeginning)
        }
        addReportDataToFile(reportFile, pageArticleCountMap)
    }

    private fun getParentPageIdText(page:Page):String{
        if (page.isTopLevelPage()){
            return "-"
        }
        return page.parentPageId!!
    }

    private fun getIsWeeklyText(page:Page):String{
        if (page.weekly){
            return "Y"
        }
        return "-"
    }
}