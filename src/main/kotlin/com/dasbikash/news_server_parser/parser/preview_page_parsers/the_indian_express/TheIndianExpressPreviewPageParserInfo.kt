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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_indian_express

internal object TheIndianExpressPreviewPageParserInfo {

    @JvmField val ARTICLE_PREVIEW_BLOCK_SELECTOR = arrayOf(".articles", ".leadstory,.opi-story", ".articles ul li")

    @JvmField val ARTICLE_TITLE_ELEMENT_SELECTOR = arrayOf(".title", "h6", "h3")

    @JvmField val ARTICLE_LINK_ELEMENT_SELECTOR = arrayOf("a", "a", "a")
    @JvmField val ARTICLE_LINK_TEXT_SELECTOR_TAG = arrayOf("href", "href", "href")

    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = arrayOf("img[data-lazy-src]", "img[data-lazy-src]", "img[data-lazy-src]")
    @JvmField val ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = arrayOf("data-lazy-src", "data-lazy-src", "data-lazy-src")

    @JvmField val ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR = arrayOf(".date", "", "")
    @JvmField val ARTICLE_PUBLICATION_DATE_TIME_FORMAT = arrayOf("MMMMM dd, yyyy hh:mm:ss a", "", "")
}
