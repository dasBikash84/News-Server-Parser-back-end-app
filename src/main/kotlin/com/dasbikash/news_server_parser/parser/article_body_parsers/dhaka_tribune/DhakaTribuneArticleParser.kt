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

package com.dasbikash.news_server_parser.parser.article_body_parsers.dhaka_tribune

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class DhakaTribuneArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.prothomalo.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return DhakaTribuneArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS
    }

    override fun getArticleModificationDateString(): String? {

        val dateStringElements = mDocument.select(DhakaTribuneArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)

        if (dateStringElements != null && dateStringElements.size == 1) {
            val dateTextFromPage = dateStringElements[0].text().trim { it <= ' ' }
            if (dateTextFromPage.split("at\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 2) {
                return dateTextFromPage.split("at\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            }
        }
        return null
    }

    override fun getFeaturedImageSelector(): String {
        return DhakaTribuneArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return DhakaTribuneArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return DhakaTribuneArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getArticleFragmentBlocks(): Elements {
        return mDocument.select(DhakaTribuneArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String {
        return DhakaTribuneArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return DhakaTribuneArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return DhakaTribuneArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR
    }

    companion object {

        private val TAG = "DTArtLoader"
    }
}
