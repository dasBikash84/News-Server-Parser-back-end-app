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

package com.dasbikash.news_server_parser.model

import java.sql.Blob
import java.util.*
import javax.persistence.*
import javax.sql.rowset.serial.SerialBlob

@Entity
@Table(name = DatabaseTableNames.PAGE_DOWNLOAD_REQUEST_ENTRY_TABLE_NAME)
data class PageDownloadRequestEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @Column(columnDefinition = "enum('ARTICLE_BODY','ARTICLE_PREVIEW_PAGE')")
        @Enumerated(EnumType.STRING)
        var pageDownloadRequestMode: PageDownloadRequestMode? = null,
        var responseDocumentId: String? = null, //for article download request articleId
        var requestKey: String = "", //request key on realtime database
        var link: String? = null,

        @ManyToOne(targetEntity = Page::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "pageId")
        var page: Page? = null,
        var active: Boolean = true,
        @Lob
        @Column(columnDefinition = "MEDIUMBLOB")
        var responseContent: Blob? = null,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false, updatable = false, insertable = false)
        var modified: Date? = null
) {
    companion object {

        fun getArticleBodyDownloadRequestEntryForPage(page: Page, link: String,article: Article)
                : PageDownloadRequestEntry {
            return PageDownloadRequestEntry(page = page, pageDownloadRequestMode = PageDownloadRequestMode.ARTICLE_BODY
                                            , link = link,responseDocumentId = article.id)
        }

        fun getArticlePreviewPageDownloadRequestEntryForPage(page: Page, link: String)
                : PageDownloadRequestEntry {
            return PageDownloadRequestEntry(page = page, pageDownloadRequestMode = PageDownloadRequestMode.ARTICLE_PREVIEW_PAGE
                                            , link = link,responseDocumentId = UUID.randomUUID().toString())
        }
    }

    @Transient
    fun getPageDownLoadRequest(): PageDownloadRequest =
            PageDownloadRequest(newsPaperId = page!!.newspaper!!.id, link = link!!,requestId = responseDocumentId)

    @Transient
    fun getResponseContentAsString(): String? {
        responseContent?.let {
            return (String(it.getBytes(1, it.length().toInt()), Charsets.UTF_32))
        }
        return null
    }

    @Transient
    fun setResponseContentFromServerResponse(pageDownLoadRequestResponse: PageDownLoadRequestResponse) {
        pageDownLoadRequestResponse.pageContent?.let {
            responseContent = SerialBlob(it.toByteArray(Charsets.UTF_32))
        }

    }
    fun deactivate(){
        active=false
    }

    override fun toString(): String {
        return "PageDownloadRequestEntry(id=$id, responseDocumentId=$responseDocumentId, page=${page?.id}, responseContent=${getResponseContentAsString()?.length ?: 0}, modified=$modified)"
    }

}