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
import com.dasbikash.news_server_parser.firebase.FireStoreDataUtils
import com.dasbikash.news_server_parser.firebase.FireStoreRefUtils
import com.dasbikash.news_server_parser.firebase.RealTimeDbDataUtils
import com.dasbikash.news_server_parser.model.PageDownloadRequest
import com.dasbikash.news_server_parser.model.PageDownloadRequestMode
import com.dasbikash.news_server_parser.model.ParserMode
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.google.api.gax.rpc.ApiStreamObserver
import com.google.cloud.firestore.DocumentSnapshot
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ArticleDataFetcherThroughClientTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

//    @Test
//    fun doParsingForPages() {
//        val session = DbSessionManager.getNewSession()
//        FireStoreDataUtils.nop()
//        Thread.sleep(5000)
//        DatabaseUtils.getAllPages(session).find { it.id=="PAGE_ID_378" }?.let {
//            val page = it
//            DatabaseUtils.findActivePageDownloadRequestEntryForPage(session,it)
//                    .filter { it.pageDownloadRequestMode== PageDownloadRequestMode.ARTICLE_PREVIEW_PAGE }
//                    .map {
//                        println("PageDownloadRequestEntry: ${it}")
//                        it
//                    }
//                    .first { it.responseContent!=null }
//                    .asSequence()
//                    .let {
//                        println("PageDownloadRequestEntry with content: ${it}")
//                        try {
//                            val parsingResult = PreviewPageParser
//                                    .parsePreviewPageForArticles(page, 1,
//                                            it.getResponseContentAsString()!!)
//                            parsingResult.first.asSequence().forEach { println(it) }
//                        }catch (e: Throwable) {
//                            e.printStackTrace()
//                        }
//                    }
//        }
//    }
//    @Test
//    fun testRun(){
//        val session = DbSessionManager.getNewSession()
//        val newsPaper = DatabaseUtils.getAllNewspapers(session).find { it.id=="NP_ID_6" }
//        newsPaper?.let {
//            session.detach(it)
//            val articleDataFetcherThroughClient = ArticleDataFetcherThroughClient(it,ParserMode.PARSE_THROUGH_CLIENT)
//            articleDataFetcherThroughClient.start()
//            articleDataFetcherThroughClient.join()
//        }
//    }
//    @Test
//    fun transferOldRequestsToRDb(){
//        val session = DbSessionManager.getNewSession()
//        FireStoreRefUtils.getPageDownloadRequestCollectionRef()
//                .stream(object:ApiStreamObserver<DocumentSnapshot>{
//                    override fun onNext(value: DocumentSnapshot?) {
//                        value?.let {
//                            println(it.id)
//                            val pageDownloadRequest =  it.toObject(PageDownloadRequest::class.java)!!
//                            println("PageDownloadRequest:${pageDownloadRequest}")
//                            val pageDownloadRequestEntry =
//                                    DatabaseUtils.findPageDownloadRequestEntryBYServerNodeName(session,it.id)
//                            pageDownloadRequestEntry?.let{println("pageDownloadRequestEntry:${pageDownloadRequestEntry}")}
//                            if (pageDownloadRequestEntry!=null) {
//                                val requestKey = RealTimeDbDataUtils.addPageDownloadRequest(pageDownloadRequestEntry)
//                                requestKey?.let {
//                                    pageDownloadRequestEntry.requestKey = it
//                                    DatabaseUtils.runDbTransection(session) {
//                                        session.update(pageDownloadRequestEntry)
//                                    }
//                                }
//                            }
//                            val task = FireStoreRefUtils.getPageDownloadRequestCollectionRef().document(it.id).delete()
//                            while (!task.isDone) {}
//                        }
//                    }
//
//                    override fun onError(t: Throwable?) {
//                        println("onError:${t}")
//                    }
//
//                    override fun onCompleted() {
//                        println("onCompleted")
//                    }
//                })
//        while (true){}
//    }
}