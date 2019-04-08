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

package com.dasbikash.news_server_parser.parser.article_body_parsers.prothom_alo;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class ProthomaloArticleParser extends ArticleBodyParser {

    private final String mSiteBaseAddress = "https://www.prothomalo.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String processLink(String linkText) {
        if (linkText.contains("paimages")){
            linkText = linkText.replace("paimages","paloimages");
        }
        return super.processLink(linkText);
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return ProthomaloArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return ProthomaloArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return ProthomaloArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getArticleModificationDateString() {
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[0];
    }

    private int mArticleLayoutType = 0;

    @Override
    protected Elements getArticleFragmentBlocks() {
        Elements articleFragments = mDocument.select(
                ProthomaloArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
        );
        if (articleFragments==null || articleFragments.size() == 0){
            mArticleLayoutType = 1;
            articleFragments = mDocument.select(
                    ProthomaloArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
            );
        }

        return articleFragments;
    }

    @Override
    protected String getParagraphImageSelector() {
        return ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR[mArticleLayoutType];
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR[mArticleLayoutType];
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        if (mArticleLayoutType == 0) {
            return ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR[mArticleLayoutType];
        } else {
            return null;
        }
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        if (mArticleLayoutType == 1) {
            return ProthomaloArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR[mArticleLayoutType];
        } else {
            return null;
        }
    }
}
