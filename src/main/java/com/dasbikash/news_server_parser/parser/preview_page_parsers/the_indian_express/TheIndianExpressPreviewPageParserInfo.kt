package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_indian_express

internal object TheIndianExpressPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = arrayOf(".articles", ".leadstory,.opi-story", ".articles ul li")

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = arrayOf(".title", "h6", "h3")

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = arrayOf("a", "a", "a")
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = arrayOf("href", "href", "href")

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = arrayOf("img[data-lazy-src]", "img[data-lazy-src]", "img[data-lazy-src]")
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = arrayOf("data-lazy-src", "data-lazy-src", "data-lazy-src")

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = arrayOf(".date", "", "")
    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = arrayOf("MMMMM dd, yyyy hh:mm:ss a", "", "")
}
