package com.dasbikash.news_server_parser.parser.article_body_parsers.bhorer_kagoj;

public abstract class BhorerKagojArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = "#content-p .content-p-feature img";
    static final String FEATURED_IMAGE_LINK_SELECTOR = "data-src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = ".wp-caption-text.gallery-caption";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = "#content-p p:not(p[class]),#content-p div:not(div[class])";

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".post_bar";
    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "MMM d, yyyy , hh:mm a";

    static final String DATE_STRING_SPLITTER_REGEX = "\\|";
    static final String ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR = "প্রকাশিত হয়েছে:";
    static final String ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT = "";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "data-src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";
}
