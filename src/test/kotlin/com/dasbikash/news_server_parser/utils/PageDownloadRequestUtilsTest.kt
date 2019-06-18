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
import com.dasbikash.news_server_parser.database.DbSessionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.nio.charset.Charset

internal class PageDownloadRequestUtilsTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

//    @Test
//    fun findPageDownloadRequestEntryBYServerNodeName() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.findPageDownloadRequestEntryBYServerNodeName(session, "26d755dd-538c-4af1-a29a-231eb0e59381")
//                ?.let {
//                    println(it.getResponseContentAsString())
//                }
//    }
//
//    @Test
//    fun addArticlePreviewPageDownloadRequestEntryForPage() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getAllPages(session).first().apply {
//            PageDownloadRequestUtils.addArticlePreviewPageDownloadRequestEntryForPage(
//                    session, this, "https://www.ittefaq.com.bd/court/62934/%E0%A6%8F%E0%A6%AD%E0%A6%BE%E0%A6%AC%E0%A7%87-%E0%A6%AC%E0%A6%A6%E0%A6%B2%E0%A6%BF-" +
//                    "%E0%A6%95%E0%A6%B0%E0%A6%BE-%E0%A6%B9%E0%A6%B2%E0%A7%87-%E0%A6%B8%E0%A7%8E-%E0%A6%95%E0%A6%B0%E0%A7%8D%E0%A6%AE%E0%A6%95%E0%A6" +
//                    "%B0%E0%A7%8D%E0%A6%A4%E0%A6%BE%E0%A6%B0%E0%A6%BE-%E0%A6%95%E0%A6%BE%E0%A6%9C-%E0%A6%95%E0%A6%B" +
//                    "0%E0%A6%A4%E0%A7%87-%E0%A6%A8%E0%A6%BF%E0%A6%B0%E0%A7%81%E0%A7%8E%E0%A6%B8%E0%A6%BE%E0%A6%B9%E0%A6%BF%E0%A6%A4-%E0%A6%B9%E0%A7%9F"
//            )
//        }
//        while (true) {}
//    }
//
//    @Test
//    fun addArticleBodyDownloadRequestEntryForPage() {
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getAllPages(session).first().apply {
//            PageDownloadRequestUtils.addArticleBodyDownloadRequestEntryForPage(
//                    session, this, "https://www.ittefaq.com.bd/court/62934/%E0%A6%8F%E0%A6%AD%E0%A6%BE%E0%A6%AC%E0%A7%87-%E0%A6%AC%E0%A6%A6%E0%A6%B2%E0%A6%BF-" +
//                    "%E0%A6%95%E0%A6%B0%E0%A6%BE-%E0%A6%B9%E0%A6%B2%E0%A7%87-%E0%A6%B8%E0%A7%8E-%E0%A6%95%E0%A6%B0%E0%A7%8D%E0%A6%AE%E0%A6%95%E0%A6" +
//                    "%B0%E0%A7%8D%E0%A6%A4%E0%A6%BE%E0%A6%B0%E0%A6%BE-%E0%A6%95%E0%A6%BE%E0%A6%9C-%E0%A6%95%E0%A6%B" +
//                    "0%E0%A6%A4%E0%A7%87-%E0%A6%A8%E0%A6%BF%E0%A6%B0%E0%A7%81%E0%A7%8E%E0%A6%B8%E0%A6%BE%E0%A6%B9%E0%A6%BF%E0%A6%A4-%E0%A6%B9%E0%A7%9F"
//            )
//        }
//    }
}