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

package com.dasbikash.news_server_parser.parser.article_body_parsers.the_gurdian

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class TheGurdianArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.theguardian.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getFeaturedImageSelector(): String {
        return TheGurdianArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getArticleModificationDateString(): String? {
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String>? {
        return null
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return TheGurdianArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return TheGurdianArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getArticleFragmentBlocks(): Elements? {

        val articleDataBlocks = mDocument.select(TheGurdianArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR)

        if (articleDataBlocks != null && articleDataBlocks.size == 1) {
            val articleDataBlock = articleDataBlocks[0]
            return articleDataBlock.select(TheGurdianArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
        }
        return null
    }

    override fun getParagraphImageSelector(): String {
        return TheGurdianArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return TheGurdianArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelector(): String {
        return TheGurdianArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String? {
        return null
    }

    companion object {

        private val TAG = "TGArtLoader"
    }
}
