/*
 * Copyright 2019 das.bikash.dev@gmail.com. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasbikash.news_server_parser.parser.article_body_parsers.dawn

internal object DawnArticleParserInfo {

    @JvmField val FEATURED_IMAGE_SELECTOR = ".slideshow__slide.slideshow__slide--first.slideshow__slide--horizontal img"
    @JvmField val FEATURED_IMAGE_LINK_SELECTOR = "src"
    @JvmField val FEATURED_IMAGE_CAPTION_SELECTOR_ATTR = "alt"

    @JvmField val REQUIRED_FEATURED_IMAGE_COUNT = 3
    @JvmField val REQUIRED_FEATURED_IMAGE_INDEX = 0

    @JvmField val ARTICLE_FRAGMENT_BLOCK_SELECTOR = 
            ".story__content p," +
            ".story__content figure," +
            ".story__content h1," +
            ".story__content h2," +
            ".story__content h3," +
            ".story__content h4"
    @JvmField val PARAGRAPH_IMAGE_SELECTOR = "img"
    @JvmField val PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR = "src"
    @JvmField val PARAGRAPH_IMAGE_CAPTION_SELECTOR = ".media__caption"
}
