package com.dasbikash.news_server_parser.bootstrap

import com.dasbikash.news_server_parser.utils.DisplayUtils
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
//        println(NewsPaperSettingsBootStrapFromRealTimeDb.getDefaultSettingsData())
    }

    @Test
    fun splitTest(){
        val dateTextFromPage = "প্রকাশ : ২৯ এপ্রিল, ২০১৯ ১১:০৯ আপডেট : ২৯ এপ্রিল, ২০১৯ ১২:২২"
        val dateTextFromPageBangla = DisplayUtils.banglaToEnglishDateString(dateTextFromPage)
        println(dateTextFromPageBangla)
    }
}