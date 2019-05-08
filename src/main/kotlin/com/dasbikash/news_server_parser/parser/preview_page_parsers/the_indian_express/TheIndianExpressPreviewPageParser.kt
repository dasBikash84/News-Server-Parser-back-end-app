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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_indian_express


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class TheIndianExpressPreviewPageParser : PreviewPageParser() {
    private var mArticleParserIndex = GENERAL_ARTICLE_PARSER_INDEX

    private val mSiteBaseAddress = "https://indianexpress.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }


    override fun getPageLink(): String? {

        if (mCurrentPage.linkFormat!!.matches(REGEX_FOR_OP_ED_LINK.toRegex())) {
            mArticleParserIndex = OP_ED_ARTICLE_PARSER_INDEX
        } else if (mCurrentPage.linkFormat!!.matches(REGEX_FOR_TRENDING_LINK.toRegex())) {
            mArticleParserIndex = TRENDING_ARTICLE_PARSER_INDEX
        }

        if (mCurrentPage.linkFormat!!.matches(REGEX_FOR_CITY_LINK.toRegex())) {
            mCurrentPageNumber++
        }

        return super.getPageLink()
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return TheIndianExpressPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT[mArticleParserIndex]
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(TheIndianExpressPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR[mArticleParserIndex])
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(
                TheIndianExpressPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mArticleParserIndex])[0].select(TheIndianExpressPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR[mArticleParserIndex])[0].attr(TheIndianExpressPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mArticleParserIndex]
        )
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(TheIndianExpressPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR[mArticleParserIndex])[0].attr(TheIndianExpressPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG[mArticleParserIndex])
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(TheIndianExpressPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mArticleParserIndex])[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String {
        return previewBlock.select(TheIndianExpressPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[mArticleParserIndex])[0].text()
    }

    companion object {


        private val REGEX_FOR_CITY_LINK = ".+?/cities/.+?/page.+"
        private val REGEX_FOR_OP_ED_LINK = ".+?/opinion/.+?"
        private val REGEX_FOR_TRENDING_LINK = ".+?/trending/.+?"
        private val REGEX_FOR_LINK_WITH_HTTPS = "^https:.+"
        private val GENERAL_ARTICLE_PARSER_INDEX = 0
        private val OP_ED_ARTICLE_PARSER_INDEX = 1
        private val TRENDING_ARTICLE_PARSER_INDEX = 2
    }

}