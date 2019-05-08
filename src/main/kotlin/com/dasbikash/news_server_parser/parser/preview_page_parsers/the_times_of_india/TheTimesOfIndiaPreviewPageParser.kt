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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_times_of_india


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class TheTimesOfIndiaPreviewPageParser : PreviewPageParser() {
    private var mArticleParserIndex = GENERAL_ARTICLE_PARSER_INDEX

    private val mSiteBaseAddress = "https://timesofindia.indiatimes.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getPageLink(): String? {

        if (mCurrentPage.linkFormat!!.matches(REGEX_FOR_ENT_LIFESTYLE_LINK.toRegex())) {
            mArticleParserIndex = ENT_LIFESTYLE_ARTICLE_PARSER_INDEX
        } else if (mCurrentPage.linkFormat!!.matches(REGEX_FOR_SPORTS_LINK.toRegex())) {
            mArticleParserIndex = SPORTS_ARTICLE_PARSER_INDEX
        }

        if ((mArticleParserIndex == GENERAL_ARTICLE_PARSER_INDEX || mArticleParserIndex == SPORTS_ARTICLE_PARSER_INDEX) && mCurrentPageNumber == 1) {
            if (mCurrentPage.linkVariablePartFormat != null) {
                return mCurrentPage.linkFormat!!.replace(mCurrentPage.linkVariablePartFormat!!, "")
            }
        }
        return super.getPageLink()
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT[mArticleParserIndex]
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR[mArticleParserIndex])
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(
                TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mArticleParserIndex])[0].select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR[mArticleParserIndex])[0].attr(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mArticleParserIndex]
        )
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(
                TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR[mArticleParserIndex])[0].attr(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG[mArticleParserIndex]
        )
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mArticleParserIndex])[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String {
        return if (mArticleParserIndex == ENT_LIFESTYLE_ARTICLE_PARSER_INDEX) {
            previewBlock.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[mArticleParserIndex])[0].attr(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG[mArticleParserIndex])
        } else {
            previewBlock.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[mArticleParserIndex])[0].text()
        }
    }

    companion object {

        private val TAG = "ProthomaloEditionLoader"

        private val REGEX_FOR_SPORTS_LINK = ".+?/sports/.+?/page.+"
        private val REGEX_FOR_ENT_LIFESTYLE_LINK = ".+?/(entertainment|life\\-style)/.+?"
        private val REGEX_FOR_LINK_WITH_HTTPS = "^https:.+"

        private val GENERAL_ARTICLE_PARSER_INDEX = 0
        private val ENT_LIFESTYLE_ARTICLE_PARSER_INDEX = 1
        private val SPORTS_ARTICLE_PARSER_INDEX = 2
    }
}