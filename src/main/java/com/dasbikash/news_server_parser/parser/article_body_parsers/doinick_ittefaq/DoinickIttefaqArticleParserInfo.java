package com.dasbikash.news_server_parser.parser.article_body_parsers.doinick_ittefaq;

abstract class DoinickIttefaqArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".body-content #dtl_img_block img";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = ".dtl_img_caption";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".body-content #dtl_content_block p";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";
}
