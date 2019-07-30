package com.dasbikash.news_server_parser.model

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
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

    //    @Test
//    fun UUIDToStrTest(){
//        while (true){
//            /*val str = "https://assetsds.cdnedge.bluemix.net/sites/default/files/styles/small_1/public/feature/images/pregnancy_2.jpg?itok=R8tpZHAy&timestamp=1554010217"
//            println(str.hashCode())
//            println(Int.MAX_VALUE)
//            Thread.sleep(1000)*/
//        }
//    }
    //https://www.anandabazar.com/women/women-s-day-special-jari-worker-rabeya-s-dream-is-to-make-son-a-doctor-1.963327
//    @Test
//    fun linkGenTest() {
//        val link = "https://www.anandabazar.com/women/women-s-day-special-jari-worker-rabeya-s-dream-is-to-make-son-a-doctor-1.963327"
//        val articleId = "9083977912878193479_PAGE_ID_220"
//        val pageId = "PAGE_ID_241"
//        val session = DbSessionManager.getNewSession()
//        DatabaseUtils.getAllPages(session).find { it.id==pageId }?.let {
//            println(articleId)
//            val calculatedId = Article.getArticleIdFromArticleLink(link,it)
//            println(calculatedId)
//        }
//    }
//
//    @Test
//    fun testStripedArticleId(){
//        val articleId = "9083977912878193479_PAGE_ID_220"
//        val stripedId = Article.getStripedArticleId(articleId)
//        println(articleId)
//        println(stripedId)
//        println(Article.getStripedArticleId(stripedId))
//    }
}