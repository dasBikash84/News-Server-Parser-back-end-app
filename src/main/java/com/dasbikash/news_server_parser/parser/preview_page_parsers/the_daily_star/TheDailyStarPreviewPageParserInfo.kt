package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_daily_star

internal object TheDailyStarPreviewPageParserInfo {

    @JvmField val FEATURE_BLOCK_SELECTOR = ".pane-news-col"
    @JvmField val FEATURE_NAME_TEXT_SELECTOR = "h2"

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = "li"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = ".list-content a"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "picture source"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "data-srcset"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = ".list-content a"
}
