package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_mirror;

abstract class DailyMirrorArticleParserInfo {

    static final String ARTICLE_DATA_BLOCK_SELECTOR = ".article-wrapper";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p,.in-article-image";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img[content]";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "content";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR = ".caption";

    static final String ARTICLE_MODIFICATION_DATE_STRING_BLOCK_SELECTOR = ".time-info [datetime]";
    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR_ATTR = "datetime";
    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "yyyy-MM-dd'T'HH:mm:ss'Z'";
}
