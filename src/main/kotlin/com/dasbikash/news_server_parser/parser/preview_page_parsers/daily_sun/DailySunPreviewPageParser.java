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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.daily_sun;


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DailySunPreviewPageParser extends PreviewPageParser {

    private static final String TAG = "StackTrace";
    //private static final String TAG = "NewAgeEditionLoader";

    private final String mSiteBaseAddress = "https://www.daily-sun.com";

    @Override
    protected String getPageLink() {
        mCurrentPageNumber = (mCurrentPageNumber-1)* DailySunPreviewPageParserInfo.ARTICLE_PREVIEW_COUNT_PER_PAGE;
        return super.getPageLink();
    }

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String processArticleLink(String articleLink) {
        if (articleLink.matches("^\\./.+")){
            articleLink = articleLink.substring(1);
        }
        return super.processArticleLink(articleLink);
    }

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return null;
    }

    @Override
    protected Elements getPreviewBlocks() {
        //Log.d(TAG, "getPreviewBlocks: Page: "+mPageLink);
        return mDocument.select(DailySunPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
    }

    @Override
    protected String getArticleLink(Element previewBlock) {
        return previewBlock.select(DailySunPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR).get(0).
                                    attr(DailySunPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(DailySunPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).get(0).
                                    attr(DailySunPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        String articleTitle = previewBlock.select(DailySunPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR).
                                    get(0).text();
        return articleTitle;
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        return null;
    }
}