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

package com.dasbikash.news_server_parser.parser.article_body_parsers.the_daily_star

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class TheDailyStarArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.thedailystar.net"
    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticleModificationDateString(): String? {

        val dateStringElements = mDocument.select(TheDailyStarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)

        if (dateStringElements != null && dateStringElements.size == 1) {

            var dateString = dateStringElements[0].text().trim { it <= ' ' }

            if (dateString.split(TheDailyStarArticleParserInfo.REGEX_TO_EXTRACT_LAST_MODIFIED_STRING.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 2) {
                dateString = dateString.split(TheDailyStarArticleParserInfo.REGEX_TO_EXTRACT_LAST_MODIFIED_STRING.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                return dateString
            }
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(TheDailyStarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getFeaturedImageSelector(): String {
        return TheDailyStarArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return TheDailyStarArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return TheDailyStarArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getArticleFragmentBlocks(): Elements? {
        val articleDataBlocks = mDocument.select(TheDailyStarArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR)

        if (articleDataBlocks != null && articleDataBlocks.size == 1) {
            val articleDataBlock = articleDataBlocks[0]
            return articleDataBlock.select(TheDailyStarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
        }
        return null
    }

    override fun getParagraphImageSelector(): String {
        return TheDailyStarArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return TheDailyStarArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelector(): String {
        return TheDailyStarArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String? {
        return null
    }
}
