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

import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.utils.DateUtils
import com.dasbikash.news_server_parser.utils.LoggerUtils
import org.hibernate.Session
import java.lang.StringBuilder
import java.math.BigInteger
import java.util.*

object DatabaseUtils {

    private val DB_WRITE_MAX_RETRY = 3

    fun runDbTransection(session: Session, operation: () -> Unit): Boolean {

        var retryLimit = DB_WRITE_MAX_RETRY;

        var exception: java.lang.Exception

        do {
            try {
                if (!session.transaction.isActive) {
                    session.beginTransaction()
                }
                operation()
                session.transaction.commit()
                return true
            } catch (ex: Exception) {
                ex.printStackTrace()
                exception = ex
            }
        } while (--retryLimit > 0)

        val stackTrace = mutableListOf<StackTraceElement>()
        exception.stackTrace.toCollection(stackTrace)

        try {
            if (!session.transaction.isActive) {
                session.beginTransaction()
            }
            LoggerUtils.logOnDb("Message: ${exception.message} Cause: ${exception.cause?.message} StackTrace: ${stackTrace}", session)
            session.transaction.commit()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return false
    }

    fun getAllActiveNewspapers(session: Session): List<Newspaper> {
        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val query = session.createQuery(hql, Newspaper::class.java)
        return query.list() as List<Newspaper>
    }

    fun getAllLanguages(session: Session): List<Language> {
        val hql = "FROM ${EntityClassNames.LANGUAGE}"
        val query = session.createQuery(hql, Language::class.java)
        return query.list() as List<Language>
    }

    fun getAllCountries(session: Session): List<Country> {
        val hql = "FROM ${EntityClassNames.COUNTRY}"
        val query = session.createQuery(hql, Country::class.java)
        return query.list() as List<Country>
    }

    fun getAllNewspapers(session: Session): List<Newspaper> {
        val hql = "FROM ${EntityClassNames.NEWSPAPER}"
        val query = session.createQuery(hql, Newspaper::class.java)
        return query.list() as List<Newspaper>
    }

    fun getAllPages(session: Session): List<Page> {
        val hql = "FROM ${EntityClassNames.PAGE}"
        val query = session.createQuery(hql, Page::class.java)
        return query.list() as List<Page>
    }

    fun getAllPageGroups(session: Session): List<PageGroup> {
        val hql = "FROM ${EntityClassNames.PAGE_GROUP}"
        val query = session.createQuery(hql, PageGroup::class.java)
        return query.list() as List<PageGroup>
    }

    fun getPageParsingIntervals(session: Session): List<PageParsingInterval> {
        val hql = "FROM ${EntityClassNames.PAGE_PARSING_INTERVAL}"
        val query = session.createQuery(hql, PageParsingInterval::class.java)
        return query.list() as List<PageParsingInterval>
    }

    fun getPageParsingIntervalForPage(session: Session, page: Page): PageParsingInterval? {
        val sql = "SELECT * FROM ${DatabaseTableNames.PAGE_PARSING_INTERVAL_TABLE_NAME} WHERE pageId='${page.id}'"
        try {
            @Suppress("UNCHECKED_CAST")
            val result = session.createNativeQuery(sql, PageParsingInterval::class.java).resultList as List<PageParsingInterval>
            if (result.size == 1) {
                return result.get(0)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun getLatestPageParsingHistoryForPage(session: Session, page: Page): PageParsingHistory? {
        val sql = "SELECT * FROM ${DatabaseTableNames.PAGE_PARSING_HISTORY_TABLE_NAME} WHERE pageId='${page.id}' order by created desc limit 1"
        try {
            @Suppress("UNCHECKED_CAST")
            val result = session.createNativeQuery(sql, PageParsingHistory::class.java).resultList as List<PageParsingHistory>
            if (result.size == 1) {
                return result.get(0)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun findArticleById(session: Session, id: String): Article? {
        val hql = "FROM ${EntityClassNames.ARTICLE} where id='${id}'"
        val query = session.createQuery(hql, Article::class.java)
        val resultList = query.list() as List<Article>
        if (resultList.size > 0) {
            return resultList.get(0)
        }
        return null
    }

    fun getArticleCountForPage(session: Session, pageId: String): Int {
        val sql = "SELECT COUNT(*) FROM ${DatabaseTableNames.ARTICLE_TABLE_NAME} WHERE pageId='${pageId}'"
        val result = session.createNativeQuery(sql).list() as List<Int>
        if (result.size == 1) {
            return result.get(0)
        }
        return 0
    }

    private fun insertDefaultEntryForNewspaper(session: Session, newspaper: Newspaper): NewspaperOpModeEntry? {
        val newspaperOpModeEntry = NewspaperOpModeEntry.getDefaultEntryForNewspaper(newspaper)
        if (runDbTransection(session) { session.save(newspaperOpModeEntry) }) {
            return newspaperOpModeEntry
        }
        return null
    }

    fun getNewspaperOpModeEntry(session: Session, newspaper: Newspaper): NewspaperOpModeEntry? {
        val sql = "SELECT * FROM ${DatabaseTableNames.NEWS_PAPER_OP_MODE_ENTRY_NAME} WHERE " +
                "newsPaperId='${newspaper.id}' order by created desc"
        try {
            val result = session.createNativeQuery(sql, NewspaperOpModeEntry::class.java).resultList as List<NewspaperOpModeEntry>
            if (result.size > 0) {
                return result.get(0)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return insertDefaultEntryForNewspaper(session, newspaper)
    }

    fun getArticleCountForPageOfYesterday(session: Session, page: Page, today: Date): Int {
        val yesterday = DateUtils.getYesterDay(today)
        return getArticleCountForPageBetweenTwoDates(page, yesterday, today, session)
    }

    fun getArticleCountForPageOfLastWeek(session: Session, page: Page, thisWeekFirstDay: Date): Int {
        val lastWeekFirstDay = DateUtils.getLastWeekSameDay(thisWeekFirstDay)
        return getArticleCountForPageBetweenTwoDates(page, lastWeekFirstDay, thisWeekFirstDay, session)
    }

    fun getArticleCountForPageOfLastMonth(session: Session, page: Page, anyDayOfMonth: Date): Int {
        val firstDayOfMonth = DateUtils.getFirstDayOfMonth(anyDayOfMonth)
        val firstDayOfLastMonth = DateUtils.getFirstDayOfLastMonth(anyDayOfMonth)

        return getArticleCountForPageBetweenTwoDates(page, firstDayOfLastMonth, firstDayOfMonth, session)
    }

    private fun getArticleCountForPageBetweenTwoDates(page: Page, startDate: Date, endDate: Date, session: Session): Int {
        val sqlBuilder = StringBuilder("SELECT COUNT(*) FROM ${DatabaseTableNames.ARTICLE_TABLE_NAME}")
                .append(" WHERE pageId='${page.id}' ")
                .append("AND modified>='${DateUtils.getDateStringForDb(startDate)}'")
                .append("AND modified<'${DateUtils.getDateStringForDb(endDate)}'")
        val result = session.createNativeQuery(sqlBuilder.toString()).singleResult
        return (result as BigInteger).toInt()
    }

    fun findPageDownloadRequestEntryBYServerNodeName(session: Session, serverNodeName: String): PageDownloadRequestEntry? {
        val sql = "SELECT * FROM ${DatabaseTableNames.PAGE_DOWNLOAD_REQUEST_ENTRY_TABLE_NAME} WHERE " +
                                "serverNodeName='${serverNodeName}' limit 1"
        LoggerUtils.logOnConsole(sql)
        try {
            val result = session.createNativeQuery(sql, PageDownloadRequestEntry::class.java).resultList as List<PageDownloadRequestEntry>
            if (result.size > 0) {
                return result.get(0)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
}