package com.dasbikash.news_server_parser.parser.preview_page_parsers.new_age

internal object NewAgePreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_CONTAINER = "article ul"
    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = "li"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = "a"
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = "href"

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "src"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = "a"

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = "p span"

    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = "hh:mm a MMMM dd, yyyy"
    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_CLEANER_REGEX = "^.+?(\\d+:\\d+)(am|pm)\\son(\\s.+)$"
    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_REPLACER = "$1 $2$3"

    @JvmField val ARTICLE_PREVIEW_COUNT_PER_PAGE = 10
}
