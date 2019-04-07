package com.dasbikash.news_server_parser.parser.article_body_parsers.prothom_alo;

public abstract class ProthomaloArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".featured_image img";
    static final String FEATURED_IMAGE_LINK_SELECTOR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String[] ARTICLE_FRAGMENT_BLOCK_SELECTOR = {".viewport>#ari-noscript>*:not(.palo_related_news)", ".pop-each"};

    static final String[] PARAGRAPH_IMAGE_SELECTOR = {"img", "img"};
    static final String[] PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = {"src", "src"};
    static final String[] PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = {"alt", ""};
    static final String[] PARAGRAPH_IMAGE_CAPTION_SELECTOR = {"", ".info span"};
}
