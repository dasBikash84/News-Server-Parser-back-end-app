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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.bd_pratidin


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class BdPratidinPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.bd-pratidin.com"

    /*@Override
    protected int getMaxReRunCountOnEmptyWithRepeat() {
        return MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE;
    }*/
    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getPageLink(): String? {
        mCurrentPageNumber = (mCurrentPageNumber - 1) * 12
        return super.getPageLink()
    }

    override fun getArticlePublicationDatetimeFormat(): String? {
        return null
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(BdPratidinPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(BdPratidinPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(BdPratidinPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(BdPratidinPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(BdPratidinPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(BdPratidinPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String? {
        return null
    }

    override fun processArticleLink(articleLinkStr: String): String? {
        var articleLink = articleLinkStr
        if (!articleLink.matches("^http.+".toRegex()) && !articleLink.matches("^/.+".toRegex())) {
            articleLink = "/$articleLink"
        }
        return super.processArticleLink(articleLink)
    }

    override fun processArticlePreviewImageLink(previewImageLinkStr: String): String? {
        var previewImageLink = previewImageLinkStr
        if (previewImageLink.matches("^\\./.+".toRegex())) {
            previewImageLink = previewImageLink.substring(1)
        }
        return super.processArticlePreviewImageLink(previewImageLink)
    }

    companion object {

        private val MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE = 3
    }
}