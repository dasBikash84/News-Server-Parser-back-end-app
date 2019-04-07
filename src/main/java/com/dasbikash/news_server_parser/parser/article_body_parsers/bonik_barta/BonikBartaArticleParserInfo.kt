package com.dasbikash.news_server_parser.parser.article_body_parsers.bonik_barta

internal object BonikBartaArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = "figure img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".content p,.content div:not(div[class])"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".date-pub"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "HH:mm:ss, MMM dd, yyyy"

    @JvmField val ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR = " মিনিট"
    @JvmField val ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT = ""
}
