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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.jaijaidin

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class JaiJaiDinPreviewPageParser : PreviewPageParser() {
    private val mSiteBaseAddress = "http://www.jaijaidinbd.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return JaiJaiDinPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(JaiJaiDinPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(JaiJaiDinPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(JaiJaiDinPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        val linkTagText =  previewBlock.select(JaiJaiDinPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0]
                                            .attr(JaiJaiDinPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
        return linkTagText.replace(Regex(".+url\\("),"").replace(Regex("\\).+"),"")
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(JaiJaiDinPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String {
        val articlePublicationDateString = previewBlock.select(JaiJaiDinPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR)[0].text()
        return DisplayUtils.banglaToEnglishDateString(articlePublicationDateString)
    }

    companion object {
        private val TAG = "JaiJaiDin"
    }
}