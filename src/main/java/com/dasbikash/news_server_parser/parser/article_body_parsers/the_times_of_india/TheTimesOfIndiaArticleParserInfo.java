package com.dasbikash.news_server_parser.parser.article_body_parsers.the_times_of_india;

abstract class TheTimesOfIndiaArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".highlight.clearfix img";
    static final String FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final String[] ARTICLE_FRAGMENT_BLOCK_SELECTOR = {
            ".highlight.clearfix .txt1,arttextxml",
            ".photosty_container_box.clearfix h3,.photosty_container_box.clearfix .clearfix.height .readmore_span,.photosty_container_box.clearfix .imagebox_bg",
            ".photo_block .clearfix.height,.photo_block .img_cptn,.photo_block .imgblock"
    };

    static final String[] PARAGRAPH_IMAGE_SELECTOR = {
            "img",
            "img[data-src-new]",
            "img[data-src-new]"
    };
    static final String[] PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = {
            "src",
            "data-src-new",
            "data-src-new"
    };
    static final String[] PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = {
            "alt",
            "alt-alt",
            "alt-alt"
    };
}
