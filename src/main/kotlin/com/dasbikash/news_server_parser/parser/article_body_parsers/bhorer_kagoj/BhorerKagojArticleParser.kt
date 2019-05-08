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

package com.dasbikash.news_server_parser.parser.article_body_parsers.bhorer_kagoj

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.select.Elements

class BhorerKagojArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.bhorerkagoj.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticleFragmentBlocks(): Elements {
        return mDocument.select(BhorerKagojArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR)
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getArticleModificationDateString(): String? {

        val dateStringElements = mDocument.select(BhorerKagojArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)
        //Log.d(TAG, "parseArticle: dateStringElements.size(): "+dateStringElements.size());
        if (dateStringElements != null && dateStringElements.size == 1) {
            //Log.d(TAG, "parseArticle: dateText: "+document.select(".detail-poauthor li").get(0).text().trim());
            var dateString = dateStringElements[0].text().trim { it <= ' ' }
            //Log.d(TAG, "parseArticle: init dateString:"+dateString);
            dateString = DisplayUtils.banglaToEnglishDateString(dateString)
            //Log.d(TAG, "parseArticle: after conversion:"+dateString);

            dateString = dateString.split(BhorerKagojArticleParserInfo.DATE_STRING_SPLITTER_REGEX.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].replace(BhorerKagojArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR,
                    BhorerKagojArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT).trim { it <= ' ' }
            return dateString
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return arrayOf(BhorerKagojArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS)
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return BhorerKagojArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return BhorerKagojArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR
    }

    override fun getFeaturedImageSelector(): String {
        return BhorerKagojArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getParagraphImageSelector(): String {
        return BhorerKagojArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return BhorerKagojArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return BhorerKagojArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR
    }

    companion object {
        private val TAG = "BBArticleLoader"
    }
}
