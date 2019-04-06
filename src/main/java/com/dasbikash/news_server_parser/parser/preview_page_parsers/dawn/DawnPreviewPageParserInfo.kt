package com.dasbikash.news_server_parser.parser.preview_page_parsers.dawn

internal object DawnPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = "article.story.story--large,article.story--small"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = "a"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = "a"
}
