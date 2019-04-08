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

package com.dasbikash.news_server_parser.parser


import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object JsoupConnector {

    private val TAG = "URLCon"
    val CONNECTION_TIMEOUT_MILLIS = 60000

    fun getDocument(pageUrl: String): Document? {

        var newDocument: Document? = null
        try {
            newDocument = Jsoup.connect(pageUrl).timeout(CONNECTION_TIMEOUT_MILLIS).followRedirects(true).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return newDocument
    }

}