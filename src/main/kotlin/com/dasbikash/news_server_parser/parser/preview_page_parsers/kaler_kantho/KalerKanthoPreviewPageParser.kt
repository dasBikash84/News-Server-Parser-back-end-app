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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.kaler_kantho


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class KalerKanthoPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.kalerkantho.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getPageLink(): String? {
        mCurrentPageNumber = (mCurrentPageNumber - 1) * ARTICLE_PREVIEW_COUNT_PER_PAGE
        return super.getPageLink()
    }

    override fun getArticlePublicationDatetimeFormat(): String? {
        return null
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(KalerKanthoPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(KalerKanthoPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(KalerKanthoPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(KalerKanthoPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(KalerKanthoPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(KalerKanthoPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String? {
        return null
    }

    override fun processArticleLink(articleLink: String): String? {
        var articleLink = articleLink
        if (articleLink.matches("^\\./.+".toRegex())) {
            articleLink = articleLink.substring(1)
        }
        return super.processArticleLink(articleLink)
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "KKEdLoader"
        val ARTICLE_PREVIEW_COUNT_PER_PAGE = 18
    }
}