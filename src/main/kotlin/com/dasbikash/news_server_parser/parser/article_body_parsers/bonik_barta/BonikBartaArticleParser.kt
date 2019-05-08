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

package com.dasbikash.news_server_parser.parser.article_body_parsers.bonik_barta

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.select.Elements

class BonikBartaArticleParser : ArticleBodyParser() {

    //There is an issue with 2nd image parsing

    private val mSiteBaseAddress = "http://bonikbarta.net"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun processLink(linkText: String): String {
        var linkText = linkText
        linkText = linkText.replace("https", "http")
        return super.processLink(linkText)
    }

    override fun getArticleFragmentBlocks(): Elements {
        return mDocument.select(BonikBartaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String? {
        return null
    }

    override fun getArticleModificationDateString(): String? {

        val dateStringElements = mDocument.select(BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)

        if (dateStringElements != null && dateStringElements.size == 1) {
            var dateString = dateStringElements[0].text().trim { it <= ' ' }
            dateString = dateString.replace(BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR,
                    BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT).trim { it <= ' ' }
            dateString = DisplayUtils.banglaToEnglishDateString(dateString)
            return dateString
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String {
        return BonikBartaArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String? {
        return null
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return BonikBartaArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR
    }

    override fun getFeaturedImageSelector(): String {
        return BonikBartaArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }
}
