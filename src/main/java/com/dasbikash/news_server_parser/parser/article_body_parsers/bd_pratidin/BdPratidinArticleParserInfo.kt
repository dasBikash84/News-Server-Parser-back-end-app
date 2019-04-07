package com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin

internal object BdPratidinArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".main_image img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = ".text-center"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "#newsDtl p,#newsDtl div:not(div[id],div[class])"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "#news_update_time"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "d MMMM, yyyy HH:mm"
}
