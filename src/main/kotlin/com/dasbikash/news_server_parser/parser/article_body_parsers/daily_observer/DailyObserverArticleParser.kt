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

package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_observer

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.select.Elements
import java.util.*
import javax.swing.text.html.parser.Element

class DailyObserverArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.observerbd.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticleModificationDateString(): String? {
        mDocument.select(DailyObserverArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)?.let {
            if (it.isNotEmpty()){
                return it.get(0).text()
            }
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(DailyObserverArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return DailyObserverArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return DailyObserverArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR
    }

    override fun getFeaturedImageSelector(): String {
        return DailyObserverArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getArticleFragmentBlocks(): Elements {
        val elements = Elements()
        mDocument.select(DailyObserverArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR).forEach {
            it.html(it.html().replace(Regex("<span.+?span>",RegexOption.DOT_MATCHES_ALL),""))
            elements.add(it)
        }
        return elements
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String {
        return DailyObserverArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return DailyObserverArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return DailyObserverArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR
    }
}
