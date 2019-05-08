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

package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_mirror

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class DailyMirrorArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.mirror.co.uk"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticleModificationDateString(): String? {

        val dateStringElements = mDocument.select(DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_BLOCK_SELECTOR)
        //Log.d(TAG, "parseArticle: dateStringElements.size(): "+dateStringElements.size());
        if (dateStringElements != null && dateStringElements.size > 0) {
            var dateString = ""
            if (dateStringElements.size == 2) {
                dateString = dateStringElements[1].attr(DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR_ATTR).trim { it <= ' ' }
            } else {
                dateString = dateStringElements[0].attr(DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR_ATTR).trim { it <= ' ' }
            }
            return dateString
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getArticleFragmentBlocks(): Elements? {

        val articleDataBloacks = mDocument.select(DailyMirrorArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR)
        return if (articleDataBloacks.size != 1) null else articleDataBloacks[0].select(DailyMirrorArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
    }

    override fun getParagraphImageSelector(): String {
        return DailyMirrorArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getFeaturedImageCaptionSelector(): String? {
        return null
    }

    override fun getFeaturedImageLinkSelectorAttr(): String? {
        return null
    }

    override fun getFeaturedImageSelector(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return DailyMirrorArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelector(): String {
        return DailyMirrorArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String? {
        return null
    }

    companion object {

        private val TAG = "DMALoader"
    }
}
