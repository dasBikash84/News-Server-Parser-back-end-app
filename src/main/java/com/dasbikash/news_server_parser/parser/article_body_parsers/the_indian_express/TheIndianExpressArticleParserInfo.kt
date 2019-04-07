package com.dasbikash.news_server_parser.parser.article_body_parsers.the_indian_express

internal object TheIndianExpressArticleParserInfo {

    @JvmField val ARTICLE_DATA_BLOCK_SELECTOR = ".articles"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p:not(p:has(span.custom-caption),.dnews p),span.custom-caption"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img[data-lazy-src]"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "data-lazy-src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR = ":root"

    @JvmField val ARTICLE_MODIFICATION_DATE_SELECTOR = "[itemprop='dateModified']"
    @JvmField val ARTICLE_MODIFICATION_DATE_SELECTOR_ATTR = "content"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "yyyy-MM-dd'T'HH:mm:ssZZZ"
}
