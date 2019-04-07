package com.dasbikash.news_server_parser.parser.article_body_parsers.the_daily_star

internal object TheDailyStarArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".featured-image"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "data-src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = ".caption"

    @JvmField val ARTICLE_DATA_BLOCK_SELECTOR = "article"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p,h1,h2,h3,h4,h5,h6,.media-shortcode"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR = ".caption"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "[class='small-text']"
    @JvmField val REGEX_TO_EXTRACT_LAST_MODIFIED_STRING = "LAST MODIFIED:\\s?"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "hh:mm a, MMMMM dd, yyyy"
}
