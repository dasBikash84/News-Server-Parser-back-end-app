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
            LoggerUtils.logMessage("Message: ${exception.message} Cause: ${exception.cause?.message} StackTrace: ${stackTrace}", session)
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

    fun getArticleCountForPageOfYesterday(session: Session, page: Page, today: Date):Int {
        val yesterday = DateUtils.getYesterDay(today)
        val sqlBuilder = StringBuilder("SELECT COUNT(*) FROM ${DatabaseTableNames.ARTICLE_TABLE_NAME}")
                                            .append(" WHERE pageId='${page.id}' ")
                                            .append("AND modified>='${DateUtils.getDateStringForDb(yesterday)}'")
                                            .append("AND modified<'${DateUtils.getDateStringForDb(today)}'")
        val result = session.createNativeQuery(sqlBuilder.toString()).singleResult
        return (result as BigInteger).toInt()
    }

    fun getArticleCountForPageOfLastWeek(session: Session, page: Page, thisWeekFirstDay: Date):Int {
        val lastWeekFirstDay = Calendar.getInstance()
        lastWeekFirstDay.time = thisWeekFirstDay
        lastWeekFirstDay.add(Calendar.DAY_OF_YEAR,-7)

        val sqlBuilder = StringBuilder("SELECT COUNT(*) FROM ${DatabaseTableNames.ARTICLE_TABLE_NAME}")
                                            .append(" WHERE pageId='${page.id}' ")
                                            .append("AND modified>='${DateUtils.getDateStringForDb(lastWeekFirstDay.time)}'")
                                            .append("AND modified<'${DateUtils.getDateStringForDb(thisWeekFirstDay)}'")
        val result = session.createNativeQuery(sqlBuilder.toString()).singleResult
        return (result as BigInteger).toInt()
    }

    fun getArticleCountForPageOfLastMonth(session: Session, page: Page, anyDayOfMonth: Date):Int {
        val firstDayOfMonth = DateUtils.getFirstDayOfMonth(anyDayOfMonth)
        val firstDayOfLastMonth = DateUtils.getFirstDayOfLastMonth(anyDayOfMonth)

        val sqlBuilder = StringBuilder("SELECT COUNT(*) FROM ${DatabaseTableNames.ARTICLE_TABLE_NAME}")
                                            .append(" WHERE pageId='${page.id}' ")
                                            .append("AND modified>='${DateUtils.getDateStringForDb(firstDayOfLastMonth)}'")
                                            .append("AND modified<'${DateUtils.getDateStringForDb(firstDayOfMonth)}'")
        val result = session.createNativeQuery(sqlBuilder.toString()).singleResult
        return (result as BigInteger).toInt()
    }
}