package com.dasbikash.news_server_parser.parser.preview_page_parsers.doinick_ittefaq;

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import com.dasbikash.news_server_parser.utils.DisplayUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DoinickIttefaqPreviewPageParser extends PreviewPageParser {

    //private static final String TAG = "StackTrace";
    private static final String TAG = "DIttefaqEdLoader";

    private final String mSiteBaseAddress = "http://www.ittefaq.com.bd";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return DoinickIttefaqPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT;
    }

    @Override
    protected Elements getPreviewBlocks() {
        return mDocument.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
    }

    Element mArticleLinkElement;

    @Override
    protected String getArticleLink(Element previewBlock) {

        return previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR).get(0).
                attr(DoinickIttefaqPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).get(0).
                attr(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String processArticlePreviewImageLink(String previewImageLink) {
        if (!previewImageLink.matches(".+?url\\(.+?\\).+")){
            return null;
        }
        previewImageLink = (previewImageLink.split("url\\("))[1];
        previewImageLink = (previewImageLink.split("\\)"))[0];
        return super.processArticlePreviewImageLink(previewImageLink);
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {

        return previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR).
                                get(0).text();
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        String articlePublicationDateString = previewBlock.select(DoinickIttefaqPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR).
                                                    get(0).text();
        return DisplayUtils.banglaToEnglishDateString(articlePublicationDateString);
    }
}