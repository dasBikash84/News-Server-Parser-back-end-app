package com.dasbikash.news_server_parser.parser.preview_page_parsers.dhaka_tribune;

class DhakaTribunePreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR = ".top-news";

    static final String ARTICLE_LINK_ELEMENT_SELECTOR = ".top-news-cont a";
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG = "href";

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img";
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src";

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR = ".news-title";

    static final String ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".listing-time h4";
    static final String ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "EEE, MMM d yyyy";
}
