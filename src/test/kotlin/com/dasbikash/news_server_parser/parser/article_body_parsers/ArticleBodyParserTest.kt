package com.dasbikash.news_server_parser.parser.article_body_parsers

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.Article
import com.dasbikash.news_server_parser.utils.FileUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

internal class ArticleBodyParserTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

//    @Test
//    fun getArticleBody(){
//        val session = DbSessionManager.getNewSession()
//
//        val docName = StringBuilder(System.getProperty("user.home")).append("/").append("Documents").append("/").append("gurdian_art.txt").toString()
//        /*File(docName).useLines {
//            it.asSequence().filter { it.contains("<p>") }.forEach { println(it) }
//        }
//
//        return*/
//        val articleBodyBuilder = StringBuilder()
//        File(docName).useLines {
//            it.asSequence().forEach { articleBodyBuilder.append(it) }
//        }
//
//
//        DatabaseUtils.getAllPages(session).find { it.id=="PAGE_ID_142" }?.let {
//            val article = Article(serial = 281878,id="-1842655999385206261",publicationTS = Date(),page = it,modified = Date(),
//                                    articleLink = "https://www.theguardian.com/technology/2019/jun/23/facebook-libra-cryptocurrency-poses-risks-to-global-banking")
//
//            ArticleBodyParser.getArticleBody(article/*,articleBodyBuilder.toString()*/)
//
//            println(article.articleText)
//        }
//    }
}