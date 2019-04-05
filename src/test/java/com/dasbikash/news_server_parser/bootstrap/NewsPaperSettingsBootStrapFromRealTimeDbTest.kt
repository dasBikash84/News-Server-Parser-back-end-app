package com.dasbikash.news_server_parser.bootstrap

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NewsPaperSettingsBootStrapFromRealTimeDbTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun getRealTimeDbEndPoints() {
        println(NewsPaperSettingsBootStrapFromRealTimeDb.getDefaultSettingsData())
    }
}