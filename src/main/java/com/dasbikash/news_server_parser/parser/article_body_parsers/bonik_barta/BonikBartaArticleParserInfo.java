package com.dasbikash.news_server_parser.parser.article_body_parsers.bonik_barta;

public abstract class BonikBartaArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = "figure img";
    static final String FEATURED_IMAGE_LINK_SELECTOR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".content p,.content div:not(div[class])";

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".date-pub";
    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "HH:mm:ss, MMM dd, yyyy";

    static final String ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR = " মিনিট";
    static final String ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT = "";
}
