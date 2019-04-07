package com.dasbikash.news_server_parser.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "page_parsing_history")
data class PageParsingHistory (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int?=null,
    @ManyToOne(targetEntity = Page::class,fetch = FetchType.LAZY)
    @JoinColumn(name="pageId")
    var page:Page? = null,
    var pageNumber:Int =0,
    var articleCount:Int =0,
    var created:Date? = null
){
    override fun toString(): String {
        return "PageParsingHistory(id=$id, page=${page?.name}, pageNumber=$pageNumber, articleCount=$articleCount, created=$created)"
    }
}