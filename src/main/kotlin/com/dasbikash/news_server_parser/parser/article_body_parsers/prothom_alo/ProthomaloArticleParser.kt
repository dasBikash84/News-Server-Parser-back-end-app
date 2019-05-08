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

package com.dasbikash.news_server_parser.parser.article_body_parsers.prothom_alo

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import org.jsoup.select.Elements

class ProthomaloArticleParser : ArticleBodyParser() {

    private val mSiteBaseAddress = "https://www.prothomalo.com"

    private var mArticleLayoutType = 0

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun processLink(linkText: String): String {
        var linkText = linkText
        if (linkText.contains("paimages")) {
            linkText = linkText.replace("paimages", "paloimages")
        }
        return super.processLink(linkText)
    }

    override fun getFeaturedImageCaptionSelectorAttr(): String {
        return ProthomaloArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR
    }

    override fun getFeaturedImageCaptionSelector(): String? {
        return null
    }

    override fun getFeaturedImageLinkSelectorAttr(): String {
        return ProthomaloArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR
    }

    override fun getFeaturedImageSelector(): String {
        return ProthomaloArticleParserInfo.FEATURED_IMAGE_SELECTOR
    }

    override fun getArticleModificationDateString(): String? {
        return null
    }

    override fun getArticleModificationDateStringFormats(): Array<String>? {
        return null
    }

    override fun getArticleFragmentBlocks(): Elements? {
        var articleFragments: Elements? = mDocument.select(
                ProthomaloArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
        )
        if (articleFragments == null || articleFragments.size == 0) {
            mArticleLayoutType = 1
            articleFragments = mDocument.select(
                    ProthomaloArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
            )
        }

        return articleFragments
    }

    override fun getParagraphImageSelector(): String {
        return ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR[mArticleLayoutType]
    }

    override fun getParagraphImageLinkSelectorAttr(): String {
        return ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR[mArticleLayoutType]
    }

    override fun getParagraphImageCaptionSelectorAttr(): String? {
        return if (mArticleLayoutType == 0) {
            ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR[mArticleLayoutType]
        } else {
            null
        }
    }

    override fun getParagraphImageCaptionSelector(): String? {
        return if (mArticleLayoutType == 1) {
            ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR[mArticleLayoutType]
        } else {
            null
        }
    }
}
