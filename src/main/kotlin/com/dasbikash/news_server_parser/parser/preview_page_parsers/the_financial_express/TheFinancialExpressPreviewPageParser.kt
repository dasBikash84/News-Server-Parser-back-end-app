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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_financial_express


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class TheFinancialExpressPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "http://thefinancialexpress.com.bd"

    internal var mPreviewBlockType = 0

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticlePublicationDatetimeFormat(): String? {
        return null
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
    }

    override fun getArticleLink(previewBlock: Element): String? {
        if (previewBlock.`is`(TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR2)) {
            mPreviewBlockType = 1
        }
        val articleLink: String?
        if (mPreviewBlockType == 0) {
            articleLink = previewBlock.select(TheFinancialExpressPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR[mPreviewBlockType])[0].attr(TheFinancialExpressPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mPreviewBlockType])
        } else {
            articleLink = previewBlock.attr(TheFinancialExpressPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mPreviewBlockType])
        }

        return if (articleLink == null || articleLink.trim { it <= ' ' }.length == 0) {
            null
        } else articleLink
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(
                TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG
        )
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(TheFinancialExpressPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mPreviewBlockType])[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String? {
        return null
    }

    override fun processArticleLink(articleLinkStr: String): String? {
        var articleLink = articleLinkStr
        if (!articleLink.contains(mSiteBaseAddress) &&
                !articleLink.matches("^//.+".toRegex()) &&
                !articleLink.matches("^/.+".toRegex())) {
            articleLink = "/$articleLink"
        }
        return super.processArticleLink(articleLink)
    }

    override fun processArticlePreviewImageLink(previewImageLinkStr: String): String? {
        var previewImageLink = previewImageLinkStr
        if (!previewImageLink.contains(mSiteBaseAddress) &&
                !previewImageLink.matches("^//.+".toRegex()) &&
                !previewImageLink.matches("^/.+".toRegex())) {
            previewImageLink = "/$previewImageLink"
        }
        return super.processArticlePreviewImageLink(previewImageLink)
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "TFEEdLoader"
    }
}