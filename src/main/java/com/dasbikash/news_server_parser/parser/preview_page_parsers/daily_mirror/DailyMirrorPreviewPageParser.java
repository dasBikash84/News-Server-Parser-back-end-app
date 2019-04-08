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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.daily_mirror;

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DailyMirrorPreviewPageParser extends PreviewPageParser {

    private static final String TAG = "DMEdLoader";

    private final String mSiteBaseAddress = "https://www.mirror.co.uk";
    private static final int MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE = 6;

    /*@Override
    protected int getMaxReRunCountOnEmptyWithRepeat() {
        return MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE;
    }*/

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return null;
    }

    @Override
    protected Elements getPreviewBlocks() {
        return mDocument.select(DailyMirrorPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
    }

    Elements mArticleLinkBlocks;
    Element mArticleLinkBlock;

    @Override
    protected String getArticleLink(Element previewBlock) {
        mArticleLinkBlocks = previewBlock.select(DailyMirrorPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR);
        if (mArticleLinkBlocks.size() != 1) return null;
        mArticleLinkBlock = mArticleLinkBlocks.get(0);

        return mArticleLinkBlock.attr(DailyMirrorPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(DailyMirrorPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).get(0).
                                    attr(DailyMirrorPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        return mArticleLinkBlock.text();
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        return null;
    }

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }
}