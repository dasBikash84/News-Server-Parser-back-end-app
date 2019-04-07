package com.dasbikash.news_server_parser.parser.article_body_parsers.new_age

internal object NewAgeArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = "article img.img-responsive.image1"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = "article .image1-caption"

    @JvmField val ARTICLE_DATA_BLOCK_SELECTOR = "article .postPageTest"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "article .postPageTestIn>p:not(p[class])"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"
}
