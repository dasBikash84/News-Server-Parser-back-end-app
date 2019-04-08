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

package com.dasbikash.news_server_parser.parser.article_body_parsers.bonik_barta;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import com.dasbikash.news_server_parser.utils.DisplayUtils;
import org.jsoup.select.Elements;

public class BonikBartaArticleParser extends ArticleBodyParser {

    //There is an issue with 2nd image parsing

    private final String mSiteBaseAddress = "http://bonikbarta.net";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String processLink(String linkText) {
        linkText = linkText.replace("https","http");
        return super.processLink(linkText);
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(BonikBartaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return null;
    }

    @Override
    protected String getArticleModificationDateString() {

        Elements dateStringElements =
                mDocument.select(BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);

        if (dateStringElements != null && dateStringElements.size()==1) {
            String dateString = dateStringElements.get(0).text().trim();
            dateString = dateString.replace(BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR,
                    BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT).trim();
            dateString = DisplayUtils.banglaToEnglishDateString(dateString);
            return dateString;
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{BonikBartaArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return BonikBartaArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return BonikBartaArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return BonikBartaArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }
}
