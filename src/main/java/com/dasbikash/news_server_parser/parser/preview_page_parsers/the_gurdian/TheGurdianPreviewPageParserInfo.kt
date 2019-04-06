package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_gurdian

internal object TheGurdianPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = ".fc-item__container"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = ".fc-item__header .fc-item__link"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = ".fc-item__header .js-headline-text"

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = ".fc-item__timestamp"
    @JvmField val ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG = "datetime"

    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ"
}
