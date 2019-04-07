package com.dasbikash.news_server_parser.parser.article_body_parsers.doinick_ittefaq

internal object DoinickIttefaqArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".body-content #dtl_img_block img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = ".dtl_img_caption"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = ".body-content #dtl_content_block p"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"
}
