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

package com.dasbikash.news_server_parser.parser.article_body_parsers.anando_bazar;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import com.dasbikash.news_server_parser.utils.DisplayUtils;
import org.jsoup.select.Elements;

public class AnandoBazarArticleParser extends ArticleBodyParser {

    private static final String TAG = "ABArticleLoader";

    private final String mSiteBaseAddress = "https://www.anandabazar.com";

    {
        mParagraphQuiterText.add("ভ্রম সংশোধন");
    }

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return AnandoBazarArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return AnandoBazarArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return AnandoBazarArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getArticleModificationDateString() {
        Elements dateStringElements =
                mDocument.select(AnandoBazarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);
        System.out.println("parseArticle: dateStringElements.size(): "+dateStringElements.size());
        if (dateStringElements != null && dateStringElements.size()>0) {
            String dateString = "";
            if (dateStringElements.size() > 1) {
                dateString = dateStringElements.get(1).text();
            } else {
                dateString = dateStringElements.get(0).text();
            }
            System.out.println("parseArticle: dateString: "+dateString);
            if (dateString.trim().length() > 0) {
                if (dateString.split("শেষ আপডেট\\s?:").length == 2) {
                    dateString = dateString.split("শেষ আপডেট\\s?:\\s?")[1];
                }
            }
            System.out.println("parseArticle: dateString: "+dateString);
            //Log.d(TAG, "parseArticle: dateString: "+dateString);
            if (dateString.trim().length() > 0) {
                dateString = DisplayUtils.banglaToEnglishDateString(dateString.trim());
            }
            System.out.println("parseArticle: dateString: "+dateString);
            return dateString;
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return AnandoBazarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS;
    }

    private int mArticleLayoutType = 0;

    @Override
    protected Elements getArticleFragmentBlocks() {
        Elements articleFragments = mDocument.select(
                AnandoBazarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
        );
        if (articleFragments==null || articleFragments.size() == 0){
            mArticleLayoutType = 1;
            articleFragments = mDocument.select(
                    AnandoBazarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
            );
        }
        if (articleFragments==null || articleFragments.size() == 0){
            mArticleLayoutType = 2;
            articleFragments = mDocument.select(
                    AnandoBazarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mArticleLayoutType]
            );
        }

        return articleFragments;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return AnandoBazarArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR[mArticleLayoutType];
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return AnandoBazarArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR[mArticleLayoutType];
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return AnandoBazarArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR[mArticleLayoutType];
    }
}