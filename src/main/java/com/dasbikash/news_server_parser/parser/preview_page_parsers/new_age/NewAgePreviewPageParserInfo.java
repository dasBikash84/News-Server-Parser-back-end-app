package com.dasbikash.news_server_parser.parser.preview_page_parsers.new_age;

class NewAgePreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_CONTAINER = "article ul";
    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR = "li";

    static final String ARTICLE_LINK_ELEMENT_SELECTOR = "a";
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG = "href";

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img";
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src";

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR = "a";

    static final String ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = "p span";

    static final String ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "hh:mm a MMMM dd, yyyy";
    static final String ARTICLE_PUBLICATION_DATE_TIME_CLEANER_REGEX = "^.+?(\\d+:\\d+)(am|pm)\\son(\\s.+)$";
    static final String ARTICLE_PUBLICATION_DATE_TIME_REPLACER = "$1 $2$3";

    static final int ARTICLE_PREVIEW_COUNT_PER_PAGE = 10;
}
