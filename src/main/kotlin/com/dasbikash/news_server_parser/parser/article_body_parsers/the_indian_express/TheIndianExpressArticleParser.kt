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
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class TheIndianExpressArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://indianexpress.com"
    private var mArticleDataBlock: Element? = null

    private val PARA_FILTER_TEXT = arrayOf("Indian Express App", "Express Editorial", "Express Opinion", "READ |", "Express Explained")

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    init {
        for (filterText in PARA_FILTER_TEXT) {
            mParagraphInvalidatorText.add(filterText)
        }
    }

    override fun getArticleModificationDateString(): String? {
        val articleDataBlocks = mDocument.select(TheIndianExpressArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR)

        if (articleDataBlocks.size == 1) {
            mArticleDataBlock = articleDataBlocks[0]
            if (mArticleDataBlock != null && mArticle.publicationTS == null) {
                val dateTimeBlocks = mArticleDataBlock!!.select(
                        TheIndianExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_SELECTOR
                )
                var dateTimeBlock: Element? = null
                if (dateTimeBlocks.size == 1) {
                    dateTimeBlock = dateTimeBlocks.first()
                }
                if (dateTimeBlock != null) {
                    return dateTimeBlock.attr(TheIndianExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_SELECTOR_ATTR)
                }
            }
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(TheIndianExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getArticleFragmentBlocks(): Elements? {
        return if (mArticleDataBlock != null) {
            mArticleDataBlock!!.select(TheIndianExpressArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
        } else null
    }

    override fun getParagraphImageSelector(): String {
        return TheIndianExpressArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
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
        return TheIndianExpressArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelector(): String {
        return TheIndianExpressArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String? {
        return null
    }
}
