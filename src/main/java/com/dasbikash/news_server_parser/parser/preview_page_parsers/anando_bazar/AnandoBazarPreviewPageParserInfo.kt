package com.dasbikash.news_server_parser.parser.preview_page_parsers.anando_bazar

internal object AnandoBazarPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = arrayOf("article", ".leadstory")

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = arrayOf(".image-placeholder a", ".leadstoryheading a")
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = arrayOf("href", "href")

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = arrayOf(".image-placeholder img", "img[data-original]")
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = arrayOf("src", "data-original")

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = arrayOf("h3", ".leadstoryheading a")

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = arrayOf(".date-arc")
    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "dd MMMM, yyyy HH:mm:ss"
}
