package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_gurdian;

class TheGurdianPreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR = ".fc-item__container";

    static final String ARTICLE_LINK_ELEMENT_SELECTOR = ".fc-item__header .fc-item__link";
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG = "href";

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img";
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src";

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR = ".fc-item__header .js-headline-text";

    static final String ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".fc-item__timestamp";
    static final String ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG = "datetime";

    static final String ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ";
}
