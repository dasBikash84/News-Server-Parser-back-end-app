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

import com.dasbikash.news_server_parser.database.DbNamedNativeQueries
import com.dasbikash.news_server_parser.utils.HashUtils
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList


@Entity
@Table(name = DatabaseTableNames.ARTICLE_TABLE_NAME)
@NamedNativeQuery(name = DbNamedNativeQueries.UN_PARSERD_ARTICLES_BY_NEWSPAPER_ID_NAME,
        query = DbNamedNativeQueries.UN_PARSERD_ARTICLES_BY_NEWSPAPER_ID_QUERY,
        resultClass = Article::class)
data class Article(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var serial: Int? = null,
        var id: String = "",

        @ManyToOne(targetEntity = Page::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "pageId")
        var page: Page? = null,

        var title: String? = null,
        var modificationTS: Date? = null,
        var publicationTS: Date? = null,

        @Column(name = "articleText", columnDefinition = "text")
        var articleText: String? = null,

        @ElementCollection(targetClass = ArticleImage::class)
        @CollectionTable(name = "image_links", joinColumns = [JoinColumn(name = "articleId")])
        @Column(name = "imageLink", columnDefinition = "text")
        var imageLinkList: List<ArticleImage> = ArrayList(),

        @Column(columnDefinition = "text")
        var previewImageLink: String? = null,

        @Column(columnDefinition = "text")
        var articleLink: String? = null,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "modified", nullable = false, updatable = false,insertable = false)
        var modified: Date? = null
) {
    @Transient
    fun isDownloaded(): Boolean {
        return articleText != null
    }

    fun setPublicationTs(publicationTS: Long) {
        val calander = Calendar.getInstance()
        calander.timeInMillis = publicationTS
        this.publicationTS = calander.time
    }

    fun setModificationTs(modificationTS: Long) {
        val calander = Calendar.getInstance()
        calander.timeInMillis = modificationTS
        this.modificationTS = calander.time
    }

    override fun toString(): String {
        return "Article(serial=$serial, id='$id', page=${page?.id}, title=$title, modificationTS=$modificationTS, " +
                "publicationTS=$publicationTS, articleText=${articleText?.length}, imageLinkList=${imageLinkList.size}, " +
                "previewImageLink=$previewImageLink, articleLink=$articleLink)"
    }
    companion object{
        fun getArticleIdFromArticleLink(articleLink: String,page: Page):String{
            val articleIdBuilder = StringBuilder(HashUtils.hash(articleLink)).append("_")

            if (page.isTopLevelPage()) {
                articleIdBuilder.append(page.id)
            } else {
                articleIdBuilder.append(page.parentPageId)
            }
            return articleIdBuilder.toString()
        }

        fun getStripedArticleId(articleId: String):String{
            return articleId.split(Regex("_")).get(0)
        }
    }
}