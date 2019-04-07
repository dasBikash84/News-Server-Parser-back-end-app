package com.dasbikash.news_server_parser.parser.article_body_parsers.anando_bazar

internal object AnandoBazarArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".textwrap_left img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = ".text_below_img"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = arrayOf("dd MMM, yyyy, HH:mm:ss", "dd, MMM, yyyy HH:mm:ss")

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = arrayOf("#textbody p", ".item .zoom")

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = arrayOf("img", "a")
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = arrayOf("src", "href")
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = arrayOf("alt", "data-caption")

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = "[class='abp-story-date-div2'],[class='abp-created-date hidden-sm hidden-xs'],[class='abp-story-date-div']"
}
