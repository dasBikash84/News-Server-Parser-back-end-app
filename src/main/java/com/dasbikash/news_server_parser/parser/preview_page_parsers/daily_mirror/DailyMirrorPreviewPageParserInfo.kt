package com.dasbikash.news_server_parser.parser.preview_page_parsers.daily_mirror

internal object DailyMirrorPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = ".pancake.use-image-placeholders.duet.primary .teaser"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = ".headline"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "data-src"
}
