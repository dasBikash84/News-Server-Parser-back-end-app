package com.dasbikash.news_server_parser.parser.article_body_parsers.kaler_kantho

internal object KalerkanthoArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = "img.img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = "img.img+p"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".some-class-name2 p,.some-class-name2 div:not(div[class],div[id])"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".n_author"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "d MMMM, yyyy HH:mm"
}
