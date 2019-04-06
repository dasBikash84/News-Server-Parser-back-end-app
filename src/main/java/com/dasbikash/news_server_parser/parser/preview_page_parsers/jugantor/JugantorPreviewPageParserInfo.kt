package com.dasbikash.news_server_parser.parser.preview_page_parsers.jugantor

internal object JugantorPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = ".all_news_content_block .col-md-6"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = "a"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = ".img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "style"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = "h4"

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".post_date p"
    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "dd MMM, yyyy"
}
