package com.dasbikash.news_server_parser.parser.preview_page_parsers.daily_sun

internal object DailySunPreviewPageParserInfo {
    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = "div.more div.list"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = "a"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = "a"

    @JvmField val ARTICLE_PREVIEW_COUNT_PER_PAGE = 38
}
