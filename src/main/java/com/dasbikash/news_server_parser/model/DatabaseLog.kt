package com.dasbikash.news_server_parser.model

import javax.persistence.*

@Entity
@Table(name = "general_log")
data class DatabaseLog(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,
        @Column(columnDefinition="text")
        val logMessage:String
)