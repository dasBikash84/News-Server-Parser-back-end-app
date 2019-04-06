package com.dasbikash.news_server_parser.parser.preview_page_parsers.prothom_alo;

class ProthomaloPreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_CONTAINER_SELECTOR = ".row";

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR = ".each.col_in";

    static final String ARTICLE_LINK_ELEMENT_SELECTOR = ".link_overlay";
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG = "href";

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = ".image img";
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src";

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR = ".title";

    static final String ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".time.aitm";
    static final String ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG = "data-published";

    static final String ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ";
}
