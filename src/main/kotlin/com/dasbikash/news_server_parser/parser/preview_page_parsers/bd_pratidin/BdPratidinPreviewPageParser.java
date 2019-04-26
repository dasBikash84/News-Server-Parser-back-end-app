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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.bd_pratidin;


import com.dasbikash.news_server_parser.model.Page;
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BdPratidinPreviewPageParser extends PreviewPageParser {

    private final String mSiteBaseAddress = "http://www.bd-pratidin.com";
    private static final int MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE = 3;

    /*@Override
    protected int getMaxReRunCountOnEmptyWithRepeat() {
        return MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE;
    }*/
    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getPageLink() {
        if (mCurrentPageNumber == 1){
            return mCurrentPage.getLinkFormat().
                    replace("/"+ Page.DEFAULT_LINK_TRAILING_FORMAT,"");
        }else{
            mCurrentPageNumber = (mCurrentPageNumber-1)*12;
            return super.getPageLink();
        }
    }

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return null;
    }

    @Override
    protected Elements getPreviewBlocks() {
        return mDocument.select(BdPratidinPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
    }

    @Override
    protected String getArticleLink(Element previewBlock) {
        return previewBlock.select(BdPratidinPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR).
                                    get(0).attr(BdPratidinPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(BdPratidinPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).
                                    get(0).attr(BdPratidinPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        return previewBlock.select(BdPratidinPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR).
                                    get(0).text();
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        return null;
    }

    @Override
    protected String processArticleLink(String articleLink) {
        if ((!articleLink.matches("^http.+")) && (!articleLink.matches("^/.+"))){
            articleLink = "/"+articleLink;
        }
        return super.processArticleLink(articleLink);
    }

    @Override
    protected String processArticlePreviewImageLink(String previewImageLink) {
        if (previewImageLink.matches("^\\./.+")){
            previewImageLink = previewImageLink.substring(1);
        }
        return super.processArticlePreviewImageLink(previewImageLink);
    }
}