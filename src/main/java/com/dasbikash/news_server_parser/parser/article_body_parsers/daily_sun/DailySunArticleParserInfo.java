package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_sun;

abstract class DailySunArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = "img.main_img";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR =   "article p:not(p[class])";

    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "h3:not(h3[class])";
    static final String ARTICLE_MODIFICATION_DATE_STRING_REPLACEMENT_SELECTOR = "^.+?(\\d.+)$";
    static final String ARTICLE_MODIFICATION_DATE_STRING_REPLACEMENT_STRING = "$1";
    static final String[] ARTICLE_MODIFICATION_DATE_STRING_FORMATS =
            {
                "d'st' MMMM, yyyy HH:mm:ss",
                "d'nd' MMMM, yyyy HH:mm:ss",
                "d'rd' MMMM, yyyy HH:mm:ss",
                "d'th' MMMM, yyyy HH:mm:ss"
            };
}
