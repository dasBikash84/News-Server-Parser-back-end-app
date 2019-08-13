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
import com.dasbikash.news_server_parser.exceptions.ParserNotFoundException
import com.dasbikash.news_server_parser.exceptions.generic.ParserException
import com.dasbikash.news_server_parser.exceptions.handler.ParserExceptionHandler
import com.dasbikash.news_server_parser.model.Article
import com.dasbikash.news_server_parser.model.Page
import com.dasbikash.news_server_parser.model.ParserMode
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.LoggerUtils
import java.util.*

class ArticleDataFetcherForPageSelf :ArticleDataFetcherBase(ParserMode.RUNNING) {

    override fun doParsingForPage(currentPage: Page) {

        val opMode = currentPage.newspaper!!.getOpMode(getDatabaseSession())//DatabaseUtils.getOpModeForNewsPaper(getDatabaseSession(),currentPage.newspaper!!)

        if (opMode==ParserMode.PARSE_THROUGH_CLIENT || opMode==ParserMode.OFF){
            return
        }

        if (!goForPageParsing(currentPage)) {
            return
        }

        try {
            waitForFareNetworkUsage(currentPage)
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
                is ParserException -> {ParserExceptionHandler.handleException(e)}
                else -> ParserExceptionHandler.handleException(ParserException(e))
            }
            if (opMode != ParserMode.GET_SYNCED ) {
                emptyPageAction(currentPage, parsingResult?.second ?: "")
            }
            return
        }

        val parsableArticleList =
                articleList
                        .asSequence()
                        .filter {
                            (DatabaseUtils.findArticleById(getDatabaseSession(), it.id)) == null &&
                            (DatabaseUtils.findArticleById(getDatabaseSession(), Article.getStripedArticleId(it.id))) == null
                        }
                        .toMutableList()
        //For Full repeat
        if (opMode != ParserMode.GET_SYNCED && parsableArticleList.size == 0) {
            allArticleRepeatAction(currentPage, parsingResult?.second ?: "")
            return
        }

        //Now go for article data fetching

        var newArticleCount = 0

        for (article in parsableArticleList) {
            try {
                waitForFareNetworkUsage(currentPage)
            } catch (ex: InterruptedException) {
                throw ex
            }
            try {
                ArticleBodyParser.getArticleBody(article)
            } catch (ex: ParserException) {
                ParserExceptionHandler.handleException(ex)
            } catch (ex: Throwable) {
                ParserExceptionHandler.handleException(ParserException(ex))
            }
            if (article.isDownloaded()) {
                if (article.previewImageLink == null && article.imageLinkList.size > 0) {
                    try {
                        article.previewImageLink = article.imageLinkList.first { it.link!=null }.link
                    }catch (ex:Throwable){}
                }
                if (article.publicationTS !=null && article.publicationTS!! > Date()){
                    article.publicationTS = Date()
                }
                if (article.modificationTS !=null && article.modificationTS!! > Date()){
                    article.modificationTS = Date()
                }
                DatabaseUtils.runDbTransection(getDatabaseSession()) {
                    getDatabaseSession().save(article)
                    newArticleCount++
                }
            }
        }

        savePageParsingHistory(
                currentPage, currentPageNumber, newArticleCount, parsingResult?.second ?: "")
    }
}