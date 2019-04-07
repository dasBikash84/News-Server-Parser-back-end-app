package com.dasbikash.news_server_parser.parser.article_body_parsers.the_daily_star;

abstract class TheDailyStarArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".featured-image";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "data-src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = ".caption";

    static final String ARTICLE_DATA_BLOCK_SELECTOR = "article";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p,h1,h2,h3,h4,h5,h6,.media-shortcode";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR = ".caption";

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "[class='small-text']";
    static final String REGEX_TO_EXTRACT_LAST_MODIFIED_STRING = "LAST MODIFIED:\\s?";
    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "hh:mm a, MMMMM dd, yyyy";
}
