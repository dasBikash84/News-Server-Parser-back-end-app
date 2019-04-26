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

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "article_upload_history")
data class ArticleUploadHistory (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int?=null,
    @ManyToOne(targetEntity = Article::class,fetch = FetchType.LAZY)
    @JoinColumn(name="articleId")
    var article: Article? = null,
    @Column(columnDefinition = "text")
    var targetAddress: String? = null,
    @Column(columnDefinition = "text")
    var logMessage: String? = null,
    var created:Date? = null
){
    override fun toString(): String {
        return "ArticleUploadHistory(id=$id, article=${article?.title}, targetAddress=$targetAddress, logMessage=$logMessage, created=$created)"
    }
}