package com.dasbikash.news_server_parser.parser.article_body_parsers.kaler_kantho;

public abstract class KalerkanthoArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = "img.img";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = "img.img+p";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".some-class-name2 p,.some-class-name2 div:not(div[class],div[id])";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".n_author";
    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "d MMMM, yyyy HH:mm";
}
