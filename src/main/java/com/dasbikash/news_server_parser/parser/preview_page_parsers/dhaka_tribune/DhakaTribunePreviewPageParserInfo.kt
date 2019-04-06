package com.dasbikash.news_server_parser.parser.preview_page_parsers.dhaka_tribune

internal object DhakaTribunePreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = ".top-news"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = ".top-news-cont a"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = ".news-title"

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".listing-time h4"
    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "EEE, MMM d yyyy"
}
