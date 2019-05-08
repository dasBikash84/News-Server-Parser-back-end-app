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

package com.dasbikash.news_server_parser.parser.article_body_parsers.jugantor

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.select.Elements

class JugantorArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.jugantor.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun processLink(linkText: String): String {
        var linkText = linkText
        if (linkText.matches("^\\./.+".toRegex())) {
            linkText = linkText.substring(1)
        }
        return super.processLink(linkText)
    }

    override fun getArticleModificationDateString(): String? {

        val dateStringElements = mDocument.select(JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)

        if (dateStringElements != null && dateStringElements.size == 1) {

            var dateString = dateStringElements[0].text().trim { it <= ' ' }

            dateString = DisplayUtils.banglaToEnglishDateString(dateString)

            dateString = dateString.replace(JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR.toRegex(), JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT).trim { it <= ' ' }
            return dateString
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getFeaturedImageSelector(): String {
        return JugantorArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return JugantorArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return JugantorArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getArticleFragmentBlocks(): Elements {
        return mDocument.select(JugantorArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String {
        return JugantorArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return JugantorArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return JugantorArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "JugArticleLoader"
    }
}
