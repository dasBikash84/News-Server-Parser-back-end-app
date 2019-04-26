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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_daily_star;

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TheDailyStarPreviewPageParser extends PreviewPageParser {

    private static final String TAG = "StackTrace";

    private final String mSiteBaseAddress = "https://www.thedailystar.net";

    private static final int MAX_RERUN_COUNT_FOR_EMPTY = 3;

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

   /* @Override
    protected int getMaxReRunCountOnEmpty() {
        return MAX_RERUN_COUNT_FOR_EMPTY;
    }*/

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return mCurrentPage.getLinkVariablePartFormat();
    }

    @Override
    protected Elements getPreviewBlocks() {
        System.out.println("getPreviewBlocks()");
        Elements featurePreviewBlocks =
                mDocument.select(TheDailyStarPreviewPageParserInfo.FEATURE_BLOCK_SELECTOR);
        if (featurePreviewBlocks.size()<1){
            System.out.println("featurePreviewBlocks.size()<1");
            return null;
        }
        System.out.println("featurePreviewBlocks.size():"+featurePreviewBlocks.size());
        Elements previewBlocks = new Elements();

        for (Element element :
                featurePreviewBlocks) {
            String featureName = element.select(TheDailyStarPreviewPageParserInfo.FEATURE_NAME_TEXT_SELECTOR).
                                                    get(0).html();
            if (featureName.contains("&amp;")){
                featureName = featureName.replace("&amp;","&");
            }

            if (mCurrentPage.getName().equalsIgnoreCase(featureName)){
                previewBlocks = element.select(TheDailyStarPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
                if (previewBlocks.size()>0){
                    break;
                } else {
                    return null;
                }
            }
        }
        return previewBlocks;
    }

    @Override
    protected String getArticleLink(Element previewBlock) {
        return previewBlock.select(
                TheDailyStarPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR).
                get(0).attr(TheDailyStarPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG
        );
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        //Log.d(TAG, "getArticlePreviewImageLink: previewBlock.html():"+previewBlock.html());
        return previewBlock.select(TheDailyStarPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).
                get(0).attr(TheDailyStarPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        return previewBlock.select(TheDailyStarPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR).
                                    get(0).text();
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        return mPageLink.substring(mPageLink.length()-mCurrentPage.getLinkVariablePartFormat().length(),mPageLink.length());
    }
}