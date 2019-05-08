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

package com.dasbikash.news_server_parser.parser.article_body_parsers.anando_bazar

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.select.Elements

class AnandoBazarArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.anandabazar.com"

    private var mArticleLayoutType = 0

    init {
        mParagraphQuiterText.add("ভ্রম সংশোধন")
    }

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getFeaturedImageCaptionSelector(): String {
        return AnandoBazarArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return AnandoBazarArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR
    }

    override fun getFeaturedImageSelector(): String {
        return AnandoBazarArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getArticleModificationDateString(): String? {
        val dateStringElements = mDocument.select(AnandoBazarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR)
        //System.out.println("parseArticle: dateStringElements.size(): "+dateStringElements.size());
        if (dateStringElements != null && dateStringElements.size > 0) {
            var dateString = ""
            if (dateStringElements.size > 1) {
                dateString = dateStringElements[1].text()
            } else {
                dateString = dateStringElements[0].text()
            }
            //System.out.println("parseArticle: dateString: "+dateString);
            if (dateString.trim { it <= ' ' }.length > 0) {
                if (dateString.split("শেষ আপডেট\\s?:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 2) {
                    dateString = dateString.split("শেষ আপডেট\\s?:\\s?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                }
            }
            //System.out.println("parseArticle: dateString: "+dateString);
            //Log.d(TAG, "parseArticle: dateString: "+dateString);
            if (dateString.trim { it <= ' ' }.length > 0) {
                dateString = DisplayUtils.banglaToEnglishDateString(dateString.trim { it <= ' ' })
            }
            //System.out.println("parseArticle: dateString: "+dateString);
            return dateString
        }
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String> {
        return AnandoBazarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS
    }

    override fun getArticleFragmentBlocks(): Elements? {
        var articleFragments: Elements? = mDocument.select(
                AnandoBazarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
        )
        if (articleFragments == null || articleFragments.size == 0) {
            mArticleLayoutType = 1
            articleFragments = mDocument.select(
                    AnandoBazarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
            )
        }
        if (articleFragments == null || articleFragments.size == 0) {
            mArticleLayoutType = 2
            articleFragments = mDocument.select(
                    AnandoBazarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
            )
        }

        return articleFragments
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return null
    }

    override fun getParagraphImageSelector(): String {
        return AnandoBazarArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR[mArticleLayoutType]
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String? {
        return null
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return AnandoBazarArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR[mArticleLayoutType]
    }

    override fun getParagraphImageCaptionSelectorAttr(): String {
        return AnandoBazarArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR[mArticleLayoutType]
    }

    companion object {

        private val TAG = "ABArticleLoader"
    }
}
