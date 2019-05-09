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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.prothom_alo


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class ProthomaloPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.prothomalo.com"


    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun processArticlePreviewImageLink(previewImageLinkStr: String): String? {
        var previewImageLink = previewImageLinkStr
        if (previewImageLink.contains(INVALID_DOMAIN_STRING)) {
            previewImageLink = previewImageLink.replace(INVALID_DOMAIN_STRING, VALID_DOMAIN_REPLACEMENT_STRING)
        }
        return super.processArticlePreviewImageLink(previewImageLink)
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return ProthomaloPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT
    }

    override fun getPreviewBlocks(): Elements? {

        val previewBlockContainers = mDocument.select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_CONTAINER_SELECTOR)
        var previewBlocks = Elements()

        if (previewBlockContainers.size == 0) {
            return null
        } else if (previewBlockContainers.size == 2) {
            previewBlocks = previewBlockContainers[0].select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
        } else {
            for (previewBlockContainer in previewBlockContainers) {
                previewBlocks.addAll(previewBlockContainer.select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR))
            }
        }
        return previewBlocks
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(ProthomaloPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR)[0].attr(ProthomaloPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG)
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "NSUtility"
        val INVALID_DOMAIN_STRING = "paimages"
        val VALID_DOMAIN_REPLACEMENT_STRING = "paloimages"
    }


}