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

package com.dasbikash.news_server_parser.utils

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.firebase.FireStoreDataUtils
import com.dasbikash.news_server_parser.model.Page
import com.dasbikash.news_server_parser.model.PageDownloadRequestEntry
import org.hibernate.Session

object PageDownloadRequestUtils {

    fun addArticlePreviewPageDownloadRequestEntryForPage(session: Session, page: Page, link: String):Boolean {
        val pageDownloadRequestEntry =
                PageDownloadRequestEntry.getArticlePreviewPageDownloadRequestEntryForPage(page, link)
        return addPageDownloadRequest(session, pageDownloadRequestEntry)
    }

    fun addArticleBodyDownloadRequestEntryForPage(session: Session, page: Page, link: String):Boolean {
        val pageDownloadRequestEntry =
                PageDownloadRequestEntry.getArticleBodyDownloadRequestEntryForPage(page, link)
        return addPageDownloadRequest(session, pageDownloadRequestEntry)
    }

    private fun addPageDownloadRequest(session: Session, pageDownloadRequestEntry: PageDownloadRequestEntry):Boolean {
        val documentId = FireStoreDataUtils.addPageDownloadRequest(pageDownloadRequestEntry.getPageDownLoadRequest())
        documentId?.let {
            pageDownloadRequestEntry.serverNodeName = it
            DatabaseUtils.runDbTransection(session) { session.save(pageDownloadRequestEntry) }
            return true
        }
        return false
    }
}