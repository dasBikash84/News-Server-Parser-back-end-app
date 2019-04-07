package com.dasbikash.news_server_parser.parser.article_body_parsers.the_financial_express

internal object TheFinancialExpressArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".card-image img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "data-src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = ".card-image .card-title-black"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "#content-part p"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "data-src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "span.p3"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "MMMM dd, yyyy HH:mm:ss"
}
