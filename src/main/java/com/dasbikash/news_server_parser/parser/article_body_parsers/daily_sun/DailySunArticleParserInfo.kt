package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_sun

internal object DailySunArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = "img.main_img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "article p:not(p[class])"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "h3:not(h3[class])"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_REPLACEMENT_SELECTOR = "^.+?(\\d.+)$"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_REPLACEMENT_STRING = "$1"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = 
            arrayOf("d'st' MMMM, yyyy HH:mm:ss", "d'nd' MMMM, yyyy HH:mm:ss", "d'rd' MMMM, yyyy HH:mm:ss", "d'th' MMMM, yyyy HH:mm:ss")
}
