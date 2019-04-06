package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_times_of_india

internal object TheTimesOfIndiaPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = arrayOf("#content>li,.list5.clearfix>li,.top-newslist li", ".md_news_box", ".top-newslist li")

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = arrayOf(".w_tle", "p", ".w_tle")

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = arrayOf("a", "a", "a")
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = arrayOf("href", "href", "href")

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = arrayOf("img", "img", "img")
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = arrayOf("data-src", "src", "data-src")

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = arrayOf("[rodate]", "[date@JvmField val]", "[rodate]")
    @JvmField val ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG = arrayOf("", "date@JvmField val", "")

    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = arrayOf("dd MMM yyyy, HH:mm", "dd MMM yyyy, HH:mm", "dd MMM yyyy, HH:mm")
}
