package com.dasbikash.news_server_parser.parser.article_body_parsers.anando_bazar;

public abstract class AnandoBazarArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".textwrap_left img";
    static final String FEATURED_IMAGE_LINK_SELECTOR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR = ".text_below_img";

    static final String[] ARTICLE_MODIFICATION_DATE_STRING_FORMATS = {"dd MMM, yyyy, HH:mm:ss","dd, MMM, yyyy HH:mm:ss"};

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR[] = {"#textbody p",".item .zoom"};

    static final String PARAGRAPH_IMAGE_SELECTOR[] = {"img","a"};
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR[] = {"src","href"};
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR[] = {"alt","data-caption"};

    static final String ARTICLE_MODIFICATION_DATE_STRING_SELECTOR =
            "[class='abp-story-date-div2'],[class='abp-created-date hidden-sm hidden-xs'],[class='abp-story-date-div']";
}
