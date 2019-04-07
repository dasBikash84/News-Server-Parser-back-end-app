package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_mirror

internal object DailyMirrorArticleParserInfo {

    @JvmField val ARTICLE_DATA_BLOCK_SELECTOR = ".article-wrapper"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = "p,.in-article-image"

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img[content]"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "content"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR = ".caption"

    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_BLOCK_SELECTOR = ".time-info [datetime]"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_SELECTOR_ATTR = "datetime"
    @JvmField val ARTICLE_MODIFICATION_DATE_STRING_FORMATS = "yyyy-MM-dd'T'HH:mm:ss'Z'"
}
