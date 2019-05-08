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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.doinick_ittefaq

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class DoinickIttefaqPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.ittefaq.com.bd"

    internal var mArticleLinkElement: Element? = null

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return DoinickIttefaqPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
    }

    override fun getArticleLink(previewBlock: Element): String {

        return previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(DoinickIttefaqPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun processArticlePreviewImageLink(previewImageLink: String): String? {
        var previewImageLink = previewImageLink
        if (!previewImageLink.matches(".+?url\\(.+?\\).+".toRegex())) {
            return null
        }
        previewImageLink = previewImageLink.split("url\\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        previewImageLink = previewImageLink.split("\\)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        return super.processArticlePreviewImageLink(previewImageLink)
    }

    override fun getArticleTitle(previewBlock: Element): String {

        return previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String {
        val articlePublicationDateString = previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR)[0].text()
        return DisplayUtils.banglaToEnglishDateString(articlePublicationDateString)
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "DIttefaqEdLoader"
    }
}