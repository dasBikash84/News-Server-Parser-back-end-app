package com.dasbikash.news_server_parser.parser.article_body_parsers.the_gurdian

internal object TheGurdianArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = "picture>img:not(.content__article-body img)"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = "figcaption"

    @JvmField val ARTICLE_DATA_BLOCK_SELECTOR = ".content__article-body"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p,h1,h2,h3,h4,h5,h6,picture"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR = "figcaption"
}
