package com.dasbikash.news_server_parser.parser.article_body_parsers.the_times_of_india

internal object TheTimesOfIndiaArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".highlight.clearfix img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = 
            arrayOf(".highlight.clearfix .txt1,arttextxml", ".photosty_container_box.clearfix h3," +
                    ".photosty_container_box.clearfix .clearfix.height .readmore_span,.photosty_container_box.clearfix .imagebox_bg", 
                    ".photo_block .clearfix.height,.photo_block .img_cptn,.photo_block .imgblock"
            )

    @JvmField val PARAGRAPH_IMAGE_SELECTOR = arrayOf("img", "img[data-src-new]", "img[data-src-new]")
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = arrayOf("src", "data-src-new", "data-src-new")
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR = arrayOf("alt", "alt-alt", "alt-alt")
}
