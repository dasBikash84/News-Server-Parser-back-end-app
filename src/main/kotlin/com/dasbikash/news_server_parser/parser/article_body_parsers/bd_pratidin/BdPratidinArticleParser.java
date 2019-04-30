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

package com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import com.dasbikash.news_server_parser.utils.DisplayUtils;
import org.jsoup.select.Elements;

public class BdPratidinArticleParser extends ArticleBodyParser {

    private static final String TAG = "BdPArtLoader";
    public static final String DATE_STRING_SPLITTER_REGEX = "প্রকাশ\\s:\\s";
    public static final String DATE_STRING_SPLITTER_KEY = "<br>";

    private final String mSiteBaseAddress = "https://www.bd-pratidin.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String processLink(String linkText) {
        if (linkText.matches("^\\./.+")) {
            linkText = linkText.substring(1);
        }
        return super.processLink(linkText);
    }

    @Override
    protected String getArticleModificationDateString() {

        Elements dateStringElements =
                mDocument.select(BdPratidinArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);

        if (dateStringElements != null && dateStringElements.size() >= 1) {
            String dateTextFromPage = dateStringElements.get(0).text().trim();
            System.out.println("dateTextFromPage" + dateTextFromPage);
            String dateTextFromPageBangla = DisplayUtils.banglaToEnglishDateString(dateTextFromPage);
            String returnString = dateTextFromPageBangla.replaceAll(".+?:\\s", "");
            System.out.println("returnString: "+returnString);
            return returnString;
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{BdPratidinArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(BdPratidinArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR;
    }
}