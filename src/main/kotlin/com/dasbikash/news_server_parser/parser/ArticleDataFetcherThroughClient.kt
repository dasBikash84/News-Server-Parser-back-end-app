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
import com.dasbikash.news_server_parser.exceptions.NewsPaperNotFoundForPageException
import com.dasbikash.news_server_parser.exceptions.generic.ParserException
import com.dasbikash.news_server_parser.exceptions.ParserNotFoundException
import com.dasbikash.news_server_parser.exceptions.handler.ParserExceptionHandler
import com.dasbikash.news_server_parser.firebase.FireStoreDataUtils
import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.LoggerUtils
import com.dasbikash.news_server_parser.utils.PageDownloadRequestUtils
import java.lang.IllegalStateException

class ArticleDataFetcherThroughClient private constructor(
        newspaper: Newspaper,
        opMode: ParserMode
) : ArticleDataFetcherForNewsPaper(newspaper, opMode) {

    companion object{
        fun getInstance(newspaper: Newspaper) =
                ArticleDataFetcherThroughClient(newspaper,ParserMode.PARSE_THROUGH_CLIENT)
    }

    init {
        FireStoreDataUtils.nop()
    }

    override fun doParsingForPages(pageListForParsing: List<Page>) {

        for (currentPage in pageListForParsing) {
//            LoggerUtils.logOnConsole("Running Parser for page ${currentPage.name} of Np: ${newspaper.name}")

            val activePageDownloadRequestEntries =
                    DatabaseUtils.findActivePageDownloadRequestEntryForPage(getDatabaseSession(), currentPage)
            activePageDownloadRequestEntries.asSequence().forEach { getDatabaseSession().refresh(it) }

            if (activePageDownloadRequestEntries.isNotEmpty()) {
//                LoggerUtils.logOnConsole("activePageDownloadRequestEntries.size for page ${currentPage.name} of Np: ${activePageDownloadRequestEntries.size}")

                if (activePageDownloadRequestEntries.filter { it.pageDownloadRequestMode == PageDownloadRequestMode.ARTICLE_PREVIEW_PAGE }.count() ==1) {

                    val articlePreviewPageDownloadRequestEntry =
                            activePageDownloadRequestEntries.find { it.pageDownloadRequestMode == PageDownloadRequestMode.ARTICLE_PREVIEW_PAGE }!!

                    if (articlePreviewPageDownloadRequestEntry.responseContent != null) {
                        //parse preview page and place article download request if needed
//                        LoggerUtils.logOnConsole("Preview page content found for page ${currentPage.name} of Np: ${newspaper.name}")

                        val currentPageNumber = getCurrentPageNumber(currentPage)
                        val articleList: MutableList<Article> = mutableListOf()

                        var parsingResult: Pair<MutableList<Article>, String>? = null
                        try {
                            parsingResult = PreviewPageParser
                                    .parsePreviewPageForArticles(currentPage, currentPageNumber,
                                            articlePreviewPageDownloadRequestEntry.getResponseContentAsString()!!)
                            articleList.addAll(parsingResult.first)
                        } catch (e: NewsPaperNotFoundForPageException) {
                            LoggerUtils.logOnConsole("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                            ParserExceptionHandler.handleException(e)
                            throw InterruptedException()
                        } catch (e: ParserNotFoundException) {
                            LoggerUtils.logOnConsole("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                            ParserExceptionHandler.handleException(e)
                            throw InterruptedException()
                        } catch (e: Throwable) {
                            LoggerUtils.logOnConsole("${e::class.java.simpleName} for page: ${currentPage.name} Np: ${currentPage.newspaper?.name}")
                            when (e) {
                                is ParserException -> ParserExceptionHandler.handleException(e)
                                else -> ParserExceptionHandler.handleException(ParserException(e))
                            }
                            deactivatePageDownloadRequestEntry(articlePreviewPageDownloadRequestEntry)
                            emptyPageAction(currentPage, parsingResult?.second ?: "")
                            continue
                        }
//                        LoggerUtils.logOnConsole("${articleList.size} article preview found for page ${currentPage.name} of Np: ${newspaper.name}")

                        val parsableArticleList =
                                articleList.asSequence()
                                        .filter { DatabaseUtils.findArticleById(getDatabaseSession(), it.id) == null }
                                        .toCollection(mutableListOf())
                        //For Full repeat
//                        LoggerUtils.logOnConsole("${parsableArticleList.size} new article preview found for page ${currentPage.name} of Np: ${newspaper.name}")


                        if (parsableArticleList.size == 0) {
                            allArticleRepeatAction(currentPage, parsingResult?.second ?: "")
                        } else {
                            parsableArticleList.asSequence().forEach {
                                DatabaseUtils.runDbTransection(getDatabaseSession()) {
                                    getDatabaseSession().save(it)
//                                    LoggerUtils.logOnConsole("New article saved: ${it.id}")
                                }
                                PageDownloadRequestUtils
                                        .addArticleBodyDownloadRequestEntryForPage(getDatabaseSession(), currentPage, it)
                            }
                        }
                        deactivatePageDownloadRequestEntry(articlePreviewPageDownloadRequestEntry)
                    }else{
//                        LoggerUtils.logOnConsole("No Preview page content for page ${currentPage.name} of Np: ${newspaper.name}")
                    }
                } else /*if (activePageDownloadRequestEntries.filter { it.pageDownloadRequestMode == PageDownloadRequestMode.ARTICLE_BODY }.count() > 0)*/ {
//                    LoggerUtils.logOnConsole("activePageDownloadRequestEntries.size for article for page ${currentPage.name} of Np: ${activePageDownloadRequestEntries.size}")
                    var processedArticleCount = 0
                    var newArticleCount = 0
                    activePageDownloadRequestEntries
                            .filter { it.pageDownloadRequestMode == PageDownloadRequestMode.ARTICLE_BODY && it.responseContent != null }
                            .asSequence().forEach {
                                //parse and save articles
//                                LoggerUtils.logOnConsole(it.toString())
                                var article: Article?=null
                                try {
                                    article = DatabaseUtils.findArticleById(getDatabaseSession(),it.responseDocumentId!!)!!
                                    ArticleBodyParser.getArticleBody(article,it.getResponseContentAsString()!!)
                                } catch (ex: ParserException) {
                                    ex.printStackTrace()
                                    ParserExceptionHandler.handleException(ex)
                                } catch (ex: Throwable) {
                                    ParserExceptionHandler.handleException(ParserException(ex))
                                }
                                article?.let {
                                    if (article.isDownloaded()) {
                                        if (article.previewImageLink == null && article.imageLinkList.size > 0) {
                                            try {
                                                article.previewImageLink = article.imageLinkList.first { it.link != null }.link
                                            } catch (ex: Throwable) {
                                            }
                                        }
                                        DatabaseUtils.runDbTransection(getDatabaseSession()) {
                                            getDatabaseSession().update(article)
//                                            LoggerUtils.logOnConsole("New article body saved for article with id: ${it.id}")
                                            newArticleCount++
                                        }
                                    }
                                }
                                processedArticleCount++
                                deactivatePageDownloadRequestEntry(it)
                            }

                    if (activePageDownloadRequestEntries.filter { it.pageDownloadRequestMode == PageDownloadRequestMode.ARTICLE_BODY }.count() ==
                            processedArticleCount) {
                        savePageParsingHistory(currentPage, getCurrentPageNumber(currentPage), newArticleCount, "")
                    }
                }
            } else {
                if (goForPageParsing(currentPage)) {

                    val currentPageNumber = getCurrentPageNumber(currentPage)
                    //place preview page download request.
                    val pageLink = PreviewPageParser.getPageLinkByPageNumber(currentPage, currentPageNumber)
                    if (pageLink != null) {
                        PageDownloadRequestUtils.addArticlePreviewPageDownloadRequestEntryForPage(getDatabaseSession(), currentPage, pageLink)
                    } else {
                        emptyPageAction(currentPage, "")
                    }
                }
            }
        }
    }

    private fun getCurrentPageNumber(currentPage: Page): Int {
        val currentPageNumber: Int

        if (currentPage.isPaginated()) {
            currentPageNumber = getLastParsedPageNumber(currentPage) + 1
        } else {
            currentPageNumber = PAGE_NUMBER_NOT_APPLICABLE
        }
        return currentPageNumber
    }

    private fun deactivatePageDownloadRequestEntry(articlePreviewPagepageDownloadRequestEntry: PageDownloadRequestEntry) {
//        articlePreviewPagepageDownloadRequestEntry.deactivate()
        DatabaseUtils.runDbTransection(getDatabaseSession()) {
            getDatabaseSession().delete(articlePreviewPagepageDownloadRequestEntry)
        }
    }
}