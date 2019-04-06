package com.dasbikash.news_server_parser.parser.preview_page_parsers.jugantor;

class JugantorPreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR = ".all_news_content_block .col-md-6";

    static final String ARTICLE_LINK_ELEMENT_SELECTOR = "a";
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG = "href";

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = ".img";
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "style";

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR = "h4";

    static final String ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".post_date p";
    static final String ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "dd MMM, yyyy";
}
