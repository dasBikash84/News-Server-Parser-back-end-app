package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_financial_express

internal object TheFinancialExpressPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = ".card,.equal-cards-wrapper .local-news,.equal-cards-wrapper1 .local-news"

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR2 = ".equal-cards-wrapper .local-news,.equal-cards-wrapper1 .local-news"

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = arrayOf("h3 a", "")
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = arrayOf("href", "href")

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img"
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "data-src"

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = arrayOf("h3", "h4")
}
