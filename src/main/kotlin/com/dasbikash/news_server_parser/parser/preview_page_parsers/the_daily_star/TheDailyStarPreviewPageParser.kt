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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_daily_star

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class TheDailyStarPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.thedailystar.net"


    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticlePublicationDatetimeFormat(): String? {
        return mCurrentPage.linkVariablePartFormat
    }

    override fun getPreviewBlocks(): Elements? {
        //System.out.println("getPreviewBlocks()");
        val featurePreviewBlocks = mDocument.select(TheDailyStarPreviewPageParserInfo.FEATURE_BLOCK_SELECTOR)
        if (featurePreviewBlocks.size < 1) {
            //System.out.println("featurePreviewBlocks.size()<1");
            return null
        }
        //System.out.println("featurePreviewBlocks.size():"+featurePreviewBlocks.size());
        var previewBlocks = Elements()

        for (element in featurePreviewBlocks) {
            var featureName = element.select(TheDailyStarPreviewPageParserInfo.FEATURE_NAME_TEXT_SELECTOR)[0].html()
            if (featureName.contains("&amp;")) {
                featureName = featureName.replace("&amp;", "&")
            }

            if (mCurrentPage.name!!.equals(featureName, ignoreCase = true)) {
                previewBlocks = element.select(TheDailyStarPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
                return if (previewBlocks.size > 0) {
                    break
                } else {
                    null
                }
            }
        }
        return previewBlocks
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(
                TheDailyStarPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(TheDailyStarPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG
        )
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        //Log.d(TAG, "getArticlePreviewImageLink: previewBlock.html():"+previewBlock.html());
        return previewBlock.select(TheDailyStarPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(TheDailyStarPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(TheDailyStarPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String {
        return mPageLink.substring(mPageLink.length - mCurrentPage.linkVariablePartFormat!!.length, mPageLink.length)
    }

    companion object {

        private val TAG = "StackTrace"
    }
}