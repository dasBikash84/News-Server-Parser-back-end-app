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

package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_sun

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class DailySunArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.daily-sun.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticleModificationDateString(): String? {
        val h3Blocks = mDocument.select(DailySunArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)

//        println("h3Blocks.size(): " + h3Blocks.size)
        if (h3Blocks.size >= 4) {
            val fullDateString = h3Blocks[3].text()
//            println("fullDateString: $fullDateString")
            return fullDateString
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return DailySunArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS
    }

    override fun getFeaturedImageSelector(): String {
        return DailySunArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return DailySunArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String {
        return DailySunArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String? {
        return null
    }


    override fun getArticleFragmentBlocks(): Elements {
        return mDocument.select(DailySunArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
    }

    override fun getParagraphImageSelector(): String {
        return DailySunArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return DailySunArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return DailySunArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR
    }

    companion object {

        private val TAG = "StackTrace"
    }
}
