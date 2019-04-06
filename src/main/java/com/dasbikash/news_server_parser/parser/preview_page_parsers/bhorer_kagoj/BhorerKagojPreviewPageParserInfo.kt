package com.dasbikash.news_server_parser.parser.preview_page_parsers.bhorer_kagoj

internal object BhorerKagojPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = ".cat-normal-content-big,.cat-normal-content-other-item"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = "a"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG1 = "data-src"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG2 = "src"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = ".title"
}
