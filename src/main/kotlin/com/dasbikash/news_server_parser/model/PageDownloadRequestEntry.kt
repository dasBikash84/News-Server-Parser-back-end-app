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

import com.google.firebase.database.Exclude
import java.util.*
import javax.persistence.*

@Entity
@Table(name = DatabaseTableNames.PAGE_DOWNLOAD_REQUEST_ENTRY_TABLE_NAME)
data class PageDownloadRequestEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @Column(columnDefinition = "enum('ARTICLE_BODY','ARTICLE_PREVIEW_PAGE')")
        @Enumerated(EnumType.STRING)
        var pageDownloadRequestMode: PageDownloadRequestMode? = null,
        var serverNodeName: String? = null,
        var link: String? = null,

        @ManyToOne(targetEntity = Page::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "pageId")
        var page: Page? = null,
        var active:Boolean=true,
        @Column(columnDefinition = "text")
        var responseContent:String?=null,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false, updatable = false,insertable = false)
        var modified: Date? = null
){
        companion object{

                private fun getPageDownloadRequestEntryForPage(page: Page,pageDownloadRequestMode: PageDownloadRequestMode,link: String)
                        :PageDownloadRequestEntry{
                        return PageDownloadRequestEntry(page = page,pageDownloadRequestMode = pageDownloadRequestMode,link = link)
                }

                fun getArticleBodyDownloadRequestEntryForPage(page: Page,link: String)
                        :PageDownloadRequestEntry{
                        return getPageDownloadRequestEntryForPage(page,PageDownloadRequestMode.ARTICLE_BODY,link)
                }

                fun getArticlePreviewPageDownloadRequestEntryForPage(page: Page,link: String)
                        :PageDownloadRequestEntry{
                        return getPageDownloadRequestEntryForPage(page,PageDownloadRequestMode.ARTICLE_PREVIEW_PAGE,link)
                }
        }
        @Transient
        fun getPageDownLoadRequest():PageDownLoadRequest =
                PageDownLoadRequest(newsPaperId = page!!.newspaper!!.id,link = link!!)
}