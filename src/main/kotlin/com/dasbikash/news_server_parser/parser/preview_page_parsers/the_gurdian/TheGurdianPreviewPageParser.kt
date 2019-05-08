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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_gurdian


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class TheGurdianPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.theguardian.com"

    private val TITLE_FILTER_REGEX = "(?i).+?(" +
            "(–\\s?podcast\\s?)|" +
            "(-\\s?podcast\\s?)|" +
            "(–\\s?video\\s?)|" +
            "(-\\s?video\\s?)|" +
            "(–.+?live updates\\s?)|" +
            "(-.+?live updates\\s?)|" +
            "(–.+?live\\s?)|" +
            "(-.+?live\\s?)|" +
            "(–\\s?cartoon\\s?)|" +
            "(-\\s?cartoon\\s?)|" +
            "(–\\s?in pictures\\s?)|" +
            "(-\\s?in pictures\\s?)|" +
            "(\\s?best photos\\s?)|" +
            "(–\\s?as it happened\\s?)|" +
            "(-\\s?as it happened\\s?)" +
            ")\\s?$"


    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return TheGurdianPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT
    }

    override fun getArticlePublicationTimeStamp(previewBlock: Element): Long? {
        return null
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(TheGurdianPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(TheGurdianPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(TheGurdianPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(TheGurdianPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(TheGurdianPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticleTitle(previewBlock: Element): String? {
        val articleTitle = previewBlock.select(TheGurdianPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
        return if (articleTitle.matches(TITLE_FILTER_REGEX.toRegex())) {
            null
        } else articleTitle
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String {
        return previewBlock.select(
                TheGurdianPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR)[0].attr(TheGurdianPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG
        )
    }

    companion object {

        private val TAG = "TheGurdianPreviewPageParser"

        private val MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE = 3
    }
}