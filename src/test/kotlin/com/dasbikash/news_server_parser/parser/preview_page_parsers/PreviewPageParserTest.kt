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

package com.dasbikash.news_server_parser.parser.preview_page_parsers

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.ErrorLog
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.utils.LoggerUtils
import org.hibernate.Session
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PreviewPageParserTest {

    private lateinit var session: Session

    @BeforeEach
    fun setUp() {
        session = DbSessionManager.getNewSession()
    }

    @AfterEach
    fun tearDown() {
        session.close()
    }

    @Test
    fun parsePreviewPageForArticles() {
    }

    /*@Test
    public void testTT(){
        try {
            PreviewPageParser.parsePreviewPageForArticles(null,10);
        } catch (NewsPaperNotFoundForPageException | ParserNotFoundException e) {
            e.printStackTrace();
        } catch (PageLinkGenerationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (EmptyJsoupDocumentException e) {
            e.printStackTrace();
        } catch (EmptyArticlePreviewPageException e) {
            e.printStackTrace();
        }
    }*/

    /*@Test
    fun indianExpressParserTest() {
        DatabaseUtils.getAllPages(session).filter { it.id == "PAGE_ID_373" }.asSequence().forEach {
            println()
            println(it.toString())
            try {
                PreviewPageParser.parsePreviewPageForArticles(it, 1).apply {
                    //                    first.asSequence().forEach { println(it.toString()) }
                    LoggerUtils.logOnConsole("${first.size} articles found")
                    LoggerUtils.logOnConsole("Message:$second")

                    val firstArticle = first.get(0)
                    ArticleBodyParser.getArticleBody(firstArticle)
                    LoggerUtils.logOnConsole("articleText:${firstArticle.articleText}")
                    LoggerUtils.logOnConsole("articleText:${firstArticle}")
                    firstArticle.imageLinkList.apply {
                        if (isNotEmpty()) {
                            this.asSequence().forEach { LoggerUtils.logOnConsole("Article imageLink:${it.toString()}") }
                        }
                    }
                }
            } catch (ex: Throwable) {
                println(ErrorLog(ex).toString())
                println(ErrorLog(ex).exceptionMessage)
                println(ErrorLog(ex).exceptionCause)
//                    ex.printStackTrace()
            }
            Thread.sleep(3000L)
        }
    }*/

    /*@Test
    fun jaiJaiDinParserTest() {
        DatabaseUtils.getAllPages(session).filter { it.newspaper!!.id == "NP_ID_19" && it.hasData() }.asSequence().forEach {
            println()
            println()
            println(it.toString())
            try {
                PreviewPageParser.parsePreviewPageForArticles(it, 1).apply {
                    //                    first.asSequence().forEach { println(it.toString()) }
                    LoggerUtils.logOnConsole("${first.size} articles found")
                    LoggerUtils.logOnConsole("Message:$second")

                    val firstArticle = first.get(0)
                    LoggerUtils.logOnConsole("firstArticle:${firstArticle}")
                    ArticleBodyParser.getArticleBody(firstArticle)
                    LoggerUtils.logOnConsole("articleText:${firstArticle.articleText}")
                    firstArticle.imageLinkList.apply {
                        if (isNotEmpty()) {
                            this.asSequence().forEach { LoggerUtils.logOnConsole("Article imageLink:${it.toString()}") }
                        }
                    }
                }
            } catch (ex: Throwable) {
                println(ErrorLog(ex).toString())
                println(ErrorLog(ex).exceptionMessage)
                println(ErrorLog(ex).exceptionCause)
//                    ex.printStackTrace()
            }
            Thread.sleep(3000L)
        }
    }*/

    /*@Test
    fun amaderSomoyParserTest() {
        DatabaseUtils.getAllPages(session).filter { it.newspaper!!.id == "NP_ID_20" && it.hasData() }.asSequence().forEach {
            println()
            println()
            println(it.toString())
            try {
                PreviewPageParser.parsePreviewPageForArticles(it, 1).apply {
                    //                    first.asSequence().forEach { println(it.toString()) }
                    LoggerUtils.logOnConsole("${first.size} articles found")
                    LoggerUtils.logOnConsole("Message:$second")

                    val firstArticle = first.get(0)
                    LoggerUtils.logOnConsole("firstArticle:${firstArticle}")
                    ArticleBodyParser.getArticleBody(firstArticle)
                    LoggerUtils.logOnConsole("articleText:${firstArticle.articleText}")
                    firstArticle.imageLinkList.apply {
                        if (isNotEmpty()) {
                            this.asSequence().forEach { LoggerUtils.logOnConsole("Article imageLink:${it.toString()}") }
                        }
                    }
                }
            } catch (ex: Throwable) {
                println(ErrorLog(ex).toString())
                println(ErrorLog(ex).exceptionMessage)
                println(ErrorLog(ex).exceptionCause)
//                    ex.printStackTrace()
            }
            Thread.sleep(3000L)
        }
    }*/

    /*@Test
    fun dailyObserverParserTest() {
        DatabaseUtils.getAllPages(session).filter { it.newspaper!!.id == "NP_ID_21" && it.hasData() *//*&& it.id=="PAGE_ID_1196"*//* }*//*.take(1)*//*.asSequence().forEach {
            println()
            println()
            println(it.toString())
            try {
                PreviewPageParser.parsePreviewPageForArticles(it, 1).apply {
                    //                    first.asSequence().forEach { println(it.toString()) }
                    LoggerUtils.logOnConsole("${first.size} articles found")
                    LoggerUtils.logOnConsole("Message:$second")

                    val firstArticle = first.shuffled().get(0)
                    LoggerUtils.logOnConsole("firstArticle:${firstArticle}")
//                    first.asSequence().forEach {
//                        val firstArticle = it
                        ArticleBodyParser.getArticleBody(firstArticle)
                        LoggerUtils.logOnConsole("articleText:${firstArticle.articleText}")
                        LoggerUtils.logOnConsole("firstArticle:${firstArticle}")
                        firstArticle.imageLinkList.apply {
                            if (isNotEmpty()) {
                                this.asSequence().forEach { LoggerUtils.logOnConsole("Article imageLink:${it.toString()}") }
                            }
                        }
//                    }
                }
            } catch (ex: Throwable) {
                println(ErrorLog(ex).toString())
                println(ErrorLog(ex).exceptionMessage)
                println(ErrorLog(ex).exceptionCause)
//                    ex.printStackTrace()
            }
            Thread.sleep(3000L)
        }
    }*/

}