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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.anando_bazar

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class AnandoBazarPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.anandabazar.com"

    private var mPageLayoutType = 0

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return AnandoBazarPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT
    }

    override fun getPreviewBlocks(): Elements {
//        var previewBlocks = Elements()
        var previewBlocks = mDocument.select(AnandoBazarPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR[mPageLayoutType])
        if (previewBlocks.size == 0) {
            mPageLayoutType = 1
            previewBlocks = mDocument.select(AnandoBazarPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR[mPageLayoutType])
        }
        return previewBlocks
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(AnandoBazarPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR[mPageLayoutType])[0].attr(AnandoBazarPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mPageLayoutType])
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(AnandoBazarPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR[mPageLayoutType])[0].attr(AnandoBazarPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG[mPageLayoutType])
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(AnandoBazarPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mPageLayoutType])[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String? {
        if (mPageLayoutType == 0) {

            val dateTimeElements = previewBlock.select(AnandoBazarPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[mPageLayoutType])

            if (dateTimeElements.size > 0) {
                val dateTimeElement = dateTimeElements[0]
                var articlePublicationDateString = dateTimeElement.text().trim { it <= ' ' }
                articlePublicationDateString = DisplayUtils.banglaToEnglishDateString(articlePublicationDateString)
                return articlePublicationDateString.replace(" ,", ",")
            }
        }
        return null
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "ABEdLoader"
    }
}