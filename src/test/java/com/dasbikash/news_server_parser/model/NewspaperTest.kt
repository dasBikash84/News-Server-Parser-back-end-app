package com.dasbikash.news_server_parser.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import java.io.File
import com.fasterxml.jackson.databind.ObjectMapper


internal class NewspaperTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun getNewsPaperCount(){
        val objectMapper = ObjectMapper()
        val newsPapers =  objectMapper.readValue(File("src/main/resources/newspaper_data.json"),
                NewsPapers::class.java)
        println(newsPapers.newspapers?.size ?: 0)
    }

    private class NewsPapers{
        var newspapers:List<Newspaper>? = null;
    }


}