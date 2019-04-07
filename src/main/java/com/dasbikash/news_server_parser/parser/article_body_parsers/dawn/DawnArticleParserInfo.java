package com.dasbikash.news_server_parser.parser.article_body_parsers.dawn;

public abstract class DawnArticleParserInfo {

    static final String FEATURED_IMAGE_SELECTOR = ".slideshow__slide.slideshow__slide--first.slideshow__slide--horizontal img";
    static final String FEATURED_IMAGE_LINK_SELECTOR = "src";
    static final String FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt";

    static final int REQUIRED_FEATURED_IMAGE_COUNT = 3;
    static final int REQUIRED_FEATURED_IMAGE_INDEX = 0;

    static final String ARTICLE_FRAGMENT_BLOCK_SELECTOR =       ".story__content p," +
                                                                ".story__content figure," +
                                                                ".story__content h1," +
                                                                ".story__content h2," +
                                                                ".story__content h3," +
                                                                ".story__content h4";
    static final String PARAGRAPH_IMAGE_SELECTOR = "img";
    static final String PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src";
    static final String PARAGRAPH_IMAGE_CAPTION_SELECTOR = ".media__caption";
}
