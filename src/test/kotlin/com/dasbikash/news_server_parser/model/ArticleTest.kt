package com.dasbikash.news_server_parser.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ArticleTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {

    }
    @Test
    fun UUIDToStrTest(){
        while (true){
            val str = "https://assetsds.cdnedge.bluemix.net/sites/default/files/styles/small_1/public/feature/images/pregnancy_2.jpg?itok=R8tpZHAy&timestamp=1554010217"
            println(str.hashCode())
            println(Int.MAX_VALUE)
            Thread.sleep(1000)
        }
    }
}