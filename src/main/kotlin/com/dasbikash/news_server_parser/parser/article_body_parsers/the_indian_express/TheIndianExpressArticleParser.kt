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

package com.dasbikash.news_server_parser.parser.article_body_parsers.the_indian_express

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class TheIndianExpressArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://indianexpress.com"

    private val PARA_FILTER_TEXT = arrayOf("Indian Express App", "Express Editorial", "Express Opinion", "READ |", "Express Explained")

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    init {
        for (filterText in PARA_FILTER_TEXT) {
            mParagraphInvalidatorText.add(filterText)
        }
    }

    override fun getReqFeaturedImageCount(): Int {
        return FEATURED_IMAGE_COUNT_FOR_MUILIPLE_IMAGE
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return emptyArray()
    }

    override fun getArticleModificationDateString(): String {
        return ""
    }

    override fun getArticleFragmentBlocks(): Elements? {
        mDocument.select(TheIndianExpressArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR).apply {
            if (isNotEmpty()){
                return this.get(0).select(TheIndianExpressArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
            }
        }
        return null
    }

    override fun getFeaturedImageSelector(): String? {
        return TheIndianExpressArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String? {
        return TheIndianExpressArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String? {
        return TheIndianExpressArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String {
        return ""
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return ""
    }

    override fun getParagraphImageCaptionSelector(): String {
        return ""
    }

    override fun getParagraphImageCaptionSelectorAttr(): String? {
        return null
    }
}
