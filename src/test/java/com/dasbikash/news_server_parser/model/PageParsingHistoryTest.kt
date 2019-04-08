package com.dasbikash.news_server_parser.model

import com.dasbikash.news_server_parser.database.DbSessionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

internal class PageParsingHistoryTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }
    @Test
    fun readWriteTest(){
        var session = DbSessionManager.getNewSession();

        session.beginTransaction()
        session.save(Country("a","b","c"))
        session.transaction.commit()

    }
}