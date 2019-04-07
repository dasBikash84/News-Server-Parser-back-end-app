package com.dasbikash.news_server_parser.parser.article_body_parsers.dhaka_tribune

internal object DhakaTribuneArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".reports-big-img>img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = ".media-caption"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".report-content p:not(p[class]),.report-content div:not(div[class])"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"


    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = 
            arrayOf("hh:mm a MMMM d'st', yyyy", "hh:mm a MMMM d'nd', yyyy", "hh:mm a MMMM d'rd', yyyy", "hh:mm a MMMM d'th', yyyy")

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".detail-poauthor li"
}
