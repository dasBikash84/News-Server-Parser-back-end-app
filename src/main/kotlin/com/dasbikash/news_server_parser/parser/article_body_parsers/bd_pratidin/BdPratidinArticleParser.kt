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

package com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.select.Elements

class BdPratidinArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.bd-pratidin.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun processLink(linkStr: String): String {
        var linkText = linkStr
        if (linkText.matches("^\\./.+".toRegex())) {
            linkText = linkText.substring(1)
        }
        return super.processLink(linkText)
    }

    override fun getArticleModificationDateString(): String? {

        val dateStringElements = mDocument.select(BdPratidinArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)

        if (dateStringElements != null && dateStringElements.size >= 1) {
            val dateTextFromPage = dateStringElements[0].text().trim { it <= ' ' }
            //System.out.println("dateTextFromPage" + dateTextFromPage);
            val dateTextFromPageBangla = DisplayUtils.banglaToEnglishDateString(dateTextFromPage)
//System.out.println("returnString: "+returnString);
            return dateTextFromPageBangla.replace(".+?:\\s".toRegex(), "")
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(BdPratidinArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR
    }

    override fun getFeaturedImageSelector(): String {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getArticleFragmentBlocks(): Elements {
        return mDocument.select(BdPratidinArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR
    }

    companion object {

        private val TAG = "BdPArtLoader"
        val DATE_STRING_SPLITTER_REGEX = "প্রকাশ\\s:\\s"
        val DATE_STRING_SPLITTER_KEY = "<br>"
    }
}
