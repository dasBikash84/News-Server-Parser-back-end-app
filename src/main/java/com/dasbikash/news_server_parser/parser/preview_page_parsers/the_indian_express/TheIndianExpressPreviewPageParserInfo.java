package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_indian_express;

class TheIndianExpressPreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR[] = {".articles",".leadstory,.opi-story",".articles ul li"};

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR[] = {".title","h6","h3"};

    static final String ARTICLE_LINK_ELEMENT_SELECTOR[] = {"a","a","a"};
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG[] = {"href","href","href"};

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR[] = {"img[data-lazy-src]","img[data-lazy-src]","img[data-lazy-src]"};
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG[] = {"data-lazy-src","data-lazy-src","data-lazy-src"};

    static final String ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[] = {".date","",""};
    static final String ARTICLE_PUBLICATION_DATE_TIME_FORMAT[] = {"MMMMM dd, yyyy hh:mm:ss a","",""};
}
