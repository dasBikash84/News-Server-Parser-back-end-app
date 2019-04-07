package com.dasbikash.news_server_parser.parser.article_body_parsers.bhorer_kagoj

internal object BhorerKagojArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = "#content-p .content-p-feature img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR = "data-src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR = ".wp-caption-text.gallery-caption"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "#content-p p:not(p[class]),#content-p div:not(div[class])"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR = ".post_bar"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "MMM d, yyyy , hh:mm a"

    @JvmField val DATE_STRING_SPLITTER_REGEX = "\\|"
    @JvmField val ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR = "প্রকাশিত হয়েছে:"
    @JvmField val ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT = ""

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "data-src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = "alt"
}
