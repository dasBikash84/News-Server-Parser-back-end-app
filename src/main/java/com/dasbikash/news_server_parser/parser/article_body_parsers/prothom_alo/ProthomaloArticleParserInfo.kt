package com.dasbikash.news_server_parser.parser.article_body_parsers.prothom_alo

internal object ProthomaloArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".featured_image img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = arrayOf(".viewport>#ari-noscript>*:not(.palo_related_news)", ".pop-each")

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = arrayOf("img", "img")
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = arrayOf("src", "src")
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = arrayOf("alt", "")
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR = arrayOf("", ".info span")
}
