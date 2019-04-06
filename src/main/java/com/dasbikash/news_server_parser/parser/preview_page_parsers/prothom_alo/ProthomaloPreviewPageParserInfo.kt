package com.dasbikash.news_server_parser.parser.preview_page_parsers.prothom_alo

internal object ProthomaloPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_CONTAINER_SELECTOR = ".row"

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = ".each.col_in"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = ".link_overlay"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = ".image img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = ".title"

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".time.aitm"
    @JvmField val ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG = "data-published"

    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ"
}
