package com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin;

public abstract class BdPratidinArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".main_image img";
    static final String FEATURED_IMAGE_LINK_SELECTOR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = ".text-center";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = "#newsDtl p,#newsDtl div:not(div[id],div[class])";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "#news_update_time";
    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "d MMMM, yyyy HH:mm";
}
