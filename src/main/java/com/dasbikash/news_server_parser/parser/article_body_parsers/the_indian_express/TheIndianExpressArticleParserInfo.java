package com.dasbikash.news_server_parser.parser.article_body_parsers.the_indian_express;

abstract class TheIndianExpressArticleParserInfo {

    static final String ARTICLE_DATA_BLOCK_SELECTOR = ".articles";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p:not(p:has(span.custom-caption),.dnews p),span.custom-caption";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img[data-lazy-src]";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "data-lazy-src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR = ":root";

    static final String ARTICLE_MODIFICATION_DATE_SELECTOR = "[itemprop='dateModified']";
    static final String ARTICLE_MODIFICATION_DATE_SELECTOR_ATTR = "content";
    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "yyyy-MM-dd'T'HH:mm:ssZZZ";
}
