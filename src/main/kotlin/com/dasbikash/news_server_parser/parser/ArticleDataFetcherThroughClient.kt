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
import com.dasbikash.news_server_parser.exceptions.ParserException
import com.dasbikash.news_server_parser.exceptions.ParserNotFoundException
import com.dasbikash.news_server_parser.exceptions.handler.ParserExceptionHandler
import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.LoggerUtils
import java.lang.IllegalStateException

class ArticleDataFetcherThroughClient(
        newspaper: Newspaper,
        opMode: ParserMode
) : ArticleDataFetcherForNewsPaper(newspaper, opMode) {


    override fun doParsingForPages(pageListForParsing: List<Page>) {

        if (opMode !=ParserMode.PARSE_THROUGH_CLIENT){
            throw IllegalStateException()
        }

        for (currentPage in pageListForParsing) {

            val pendingPageDownloadRequestEntries =
                    DatabaseUtils.findPendingPageDownloadRequestEntryForPage(getDatabaseSession(),currentPage)

            if (pendingPageDownloadRequestEntries.isNotEmpty()){

                if (pendingPageDownloadRequestEntries.filter { it.pageDownloadRequestMode==PageDownloadRequestMode.ARTICLE_PREVIEW_PAGE }.count()>0){

                    val articlePreviewPagepageDownloadRequestEntry =
                            pendingPageDownloadRequestEntries.filter { it.pageDownloadRequestMode==PageDownloadRequestMode.ARTICLE_PREVIEW_PAGE }.first()

                    if (articlePreviewPagepageDownloadRequestEntry.responseContent!=null){
                        //parse preview page and place article download request if needed
                    }
                }else if (pendingPageDownloadRequestEntries.filter { it.pageDownloadRequestMode==PageDownloadRequestMode.ARTICLE_BODY }.count()>0){
                    pendingPageDownloadRequestEntries.filter { it.pageDownloadRequestMode==PageDownloadRequestMode.ARTICLE_BODY && it.responseContent!=null }
                            .asSequence().forEach {
                                //parse and save articles
                            }
                }
            }else{
                if (goForPageParsing(currentPage)){
                    //place preview page download request.
                }
            }

            //first check if any page download request is pending or not
            //if pending then get details of that which will contain server address for
            //downloaded request

            //in case of pending download request check server if requested page downloaded or not.
            //if downloaded then process the response

            //if no download request pending then check and add new download request if required


            /*if (opMode != ParserMode.GET_SYNCED && !goForPageParsing(currentPage)) {
                continue
            }

            try {
                waitForFareNetworkUsage()
            } catch (ex: InterruptedException) {
                throw ex
            }

            val currentPageNumber: Int

            if (currentPage.isPaginated()) {
                currentPageNumber = getLastParsedPageNumber(currentPage) + 1
            } else {
                currentPageNumber = PAGE_NUMBER_NOT_APPLICABLE
            }

            val articleList: MutableList<Article> = mutableListOf()

            var parsingResult: Pair<MutableList<Article>, String>? = null
            try {
                parsingResult = PreviewPageParser.parsePreviewPageForArticles(currentPage, currentPageNumber)
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
                if (opMode != ParserMode.GET_SYNCED && (e is ParserException)) {
                    emptyPageAction(currentPage, parsingResult?.second ?: "")
                }
                continue
            }

            val parseableArticleList =
                    articleList
                            .asSequence()
                            .filter {
                                DatabaseUtils.findArticleById(getDatabaseSession(), it.id) == null
                            }
                            .toCollection(mutableListOf())
            //For Full repeat
            if (opMode != ParserMode.GET_SYNCED && parseableArticleList.size == 0) {
                allArticleRepeatAction(currentPage, parsingResult?.second ?: "")
                continue
            }

            //Now go for article data fetching

            var newArticleCount = 0

            for (article in parseableArticleList) {
                try {
                    waitForFareNetworkUsage()
                } catch (ex: InterruptedException) {
                    throw ex
                }
                try {
                    ArticleBodyParser.getArticleBody(article)
                    if (article.isDownloaded()) {
                        if (article.previewImageLink == null && article.imageLinkList.size > 0) {
                            article.previewImageLink = article.imageLinkList.get(0).link
                        }
                        DatabaseUtils.runDbTransection(getDatabaseSession()) {
                            getDatabaseSession().save(article)
                            newArticleCount++
                        }
                    }
                } catch (ex: ParserException) {
                    ParserExceptionHandler.handleException(ex)
                } catch (ex: Throwable) {
                    ParserExceptionHandler.handleException(ParserException(ex))
                }
            }

            savePageParsingHistory(
                    currentPage, currentPageNumber, newArticleCount, parsingResult?.second ?: "")*/
        }
    }
}