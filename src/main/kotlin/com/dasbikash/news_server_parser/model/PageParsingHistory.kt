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
@Table(name = "page_parsing_history")
data class PageParsingHistory(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @ManyToOne(targetEntity = Page::class, fetch = FetchType.LAZY)
        @JoinColumn(name = "pageId")
        var page: Page? = null,
        var pageNumber: Int = 0,
        var articleCount: Int = 0,
        @Column(columnDefinition = "text")
        var parsingLogMessage: String = "",
        var created: Date? = Date()
) {
    override fun toString(): String {
        return "PageParsingHistory(id=$id, page=${page?.name}, pageNumber=$pageNumber, articleCount=$articleCount, created=$created)"
    }
}