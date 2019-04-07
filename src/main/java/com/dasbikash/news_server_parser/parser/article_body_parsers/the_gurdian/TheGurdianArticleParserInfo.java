package com.dasbikash.news_server_parser.parser.article_body_parsers.the_gurdian;

abstract class TheGurdianArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = "picture>img:not(.content__article-body img)";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = "figcaption";

    static final String ARTICLE_DATA_BLOCK_SELECTOR = ".content__article-body";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p,h1,h2,h3,h4,h5,h6,picture";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR = "figcaption";
}
