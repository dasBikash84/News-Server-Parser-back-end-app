package com.dasbikash.news_server_parser.parser.article_body_parsers.dhaka_tribune;

public abstract class DhakaTribuneArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".reports-big-img>img";
    static final String FEATURED_IMAGE_LINK_SELECTOR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = ".media-caption";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".report-content p:not(p[class]),.report-content div:not(div[class])";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";


    static final String[] ARTICLE_MODIFICATION_DATE_STRING_FORMATS = {
            "hh:mm a MMMM d'st', yyyy",
            "hh:mm a MMMM d'nd', yyyy",
            "hh:mm a MMMM d'rd', yyyy",
            "hh:mm a MMMM d'th', yyyy"
    };

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".detail-poauthor li";
}
