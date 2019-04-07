package com.dasbikash.news_server_parser.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ArticleImage(
        @Column(columnDefinition = "text")
        var link:String? = null,
        @Column(columnDefinition = "text")
        var caption:String? = null
)