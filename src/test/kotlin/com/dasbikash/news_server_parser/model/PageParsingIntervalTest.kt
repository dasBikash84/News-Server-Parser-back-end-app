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

package com.dasbikash.news_server_parser.model

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PageParsingIntervalTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }


//    @Test
//    fun testSaveSame() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getPageParsingIntervals(session).asSequence().forEach {
//            println(it)
//            it.parsingIntervalMS = it.parsingIntervalMS!! + 1000
//            DatabaseUtils.runDbTransection(session) {
//                session.update(it)
//            }
//        }
//
//        DatabaseUtils.getPageParsingIntervals(session).asSequence().forEach {
//            println(it)
//        }
//    }
//
//    @Test
//    fun testRecalculateForOne() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getAllPages(session).find { it.id == "PAGE_ID_163" }?.let {
//            val pageParsingInterval = PageParsingInterval.recalculate(it)
//            println(pageParsingInterval)
//            println()
//        }
//    }
//
//    @Test
//    fun testGetLatestPageParsingHistoryForPage() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getAllPages(session).asSequence().filter { it.active && it.hasData() }.forEach {
//            println(DatabaseUtils.getLatestPageParsingHistoryForPage(session,it))
//        }
//    }
//
//    @Test
//    fun testgetPageParsingIntervalForPage() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getAllPages(session).asSequence().filter { it.id == "PAGE_ID_101" }.forEach {
//            println(DatabaseUtils.getPageParsingIntervalForPage(session,it))
//        }
//    }
//
//    @Test
//    fun testRecalculate() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getAllPages(session).filter { it.hasData() && it.active && !it.weekly }.sortedBy { it.newspaper!!.id }.asSequence()
//                .map { PageParsingInterval.recalculate(it) }
//                .forEach {
//                    println(it)
//                    println()
//                    println()
//                    println()
//                }
//    }
}