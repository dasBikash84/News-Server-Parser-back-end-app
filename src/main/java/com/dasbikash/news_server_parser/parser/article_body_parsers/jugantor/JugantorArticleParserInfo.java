package com.dasbikash.news_server_parser.parser.article_body_parsers.jugantor;

public abstract class JugantorArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".dtl_section .dtl_img_section img";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = ".dtl_img_caption";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR = "#myText p";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".rpt_name p";

    static final String ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR = "^.+?(\\d{2}.+?\\d{4},\\s\\d{2}:\\d{2}).+";
    static final String ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT = "$1";

    static final String ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "dd MMMM yyyy, HH:mm";
}
