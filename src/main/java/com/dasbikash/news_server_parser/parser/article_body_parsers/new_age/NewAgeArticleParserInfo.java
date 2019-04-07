package com.dasbikash.news_server_parser.parser.article_body_parsers.new_age;

abstract class NewAgeArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = "article img.img-responsive.image1";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = "article .image1-caption";

    static final String ARTICLE_DATA_BLOCK_SELECTOR = "article .postPageTest";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR =   "article .postPageTestIn>p:not(p[class])";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";
}
