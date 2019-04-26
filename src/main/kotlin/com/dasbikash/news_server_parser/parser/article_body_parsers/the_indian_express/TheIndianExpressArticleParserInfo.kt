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
