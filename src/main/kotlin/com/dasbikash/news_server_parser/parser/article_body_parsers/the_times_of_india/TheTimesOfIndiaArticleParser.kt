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

package com.dasbikash.news_server_parser.parser.article_body_parsers.the_times_of_india

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class TheTimesOfIndiaArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://timesofindia.indiatimes.com"

    private var mParagraphLayoutType = 0

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getFeaturedImageSelector(): String {
        return TheTimesOfIndiaArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getArticleModificationDateString(): String? {
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String>? {
        return null
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return TheTimesOfIndiaArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String {
        return TheTimesOfIndiaArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String? {
        return null
    }

    override fun getArticleFragmentBlocks(): Elements? {

        var articleFragmentBlocks = mDocument.select(TheTimesOfIndiaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mParagraphLayoutType])

        if (articleFragmentBlocks.size > 0) {
            return articleFragmentBlocks
        }
        mParagraphLayoutType++

        articleFragmentBlocks = mDocument.select(TheTimesOfIndiaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mParagraphLayoutType])

        if (articleFragmentBlocks.size > 0) {
            return articleFragmentBlocks
        }
        mParagraphLayoutType++

        articleFragmentBlocks = mDocument.select(TheTimesOfIndiaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mParagraphLayoutType])

        return if (articleFragmentBlocks.size > 0) {
            articleFragmentBlocks
        } else null
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String {
        return TheTimesOfIndiaArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR[mParagraphLayoutType]
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return TheTimesOfIndiaArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR[mParagraphLayoutType]
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return TheTimesOfIndiaArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR[mParagraphLayoutType]
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "TTOIArticleLoader"
    }
}
