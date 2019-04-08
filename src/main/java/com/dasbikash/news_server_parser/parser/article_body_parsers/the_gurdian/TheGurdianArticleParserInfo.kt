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
