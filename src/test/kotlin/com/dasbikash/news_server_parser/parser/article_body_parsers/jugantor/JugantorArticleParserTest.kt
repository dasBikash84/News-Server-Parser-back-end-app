package com.dasbikash.news_server_parser.parser.article_body_parsers.jugantor

import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.parser.NEWS_PAPER_ID
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class JugantorArticleParserTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun readFirstPageArticles(){

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val  session = DbSessionManager.getNewSession()
        val query = session.createQuery(hql,Newspaper::class.java)
        val newsPapers= query.list() as List<Newspaper>

        //session.close()

        newsPapers.filter {
            it.id == NEWS_PAPER_ID.JUGANTOR.id
        }.map {
            println("Np: ${it.name}")
            //println("Page: ${it.pageList?.get(0)?.name}")
            it.pageList?.filter {
                it.hasData()
            }?.get(Random(Random(10).nextInt()).nextInt(it.pageList!!.size))
        }.map {
            it?.let {
                val articleList = PreviewPageParser.parsePreviewPageForArticles(it,1)
                it.articleList = articleList.first
                it.articleList
                        ?.forEach {
                            //session.persist(it)
                            println("Article  ${it}")
                        }
            }
            it
        }.map {
            it?.let {
                println("Article Data before parsing:"+it.articleList?.first())
                println("Article Data after parsing:"+ ArticleBodyParser.getArticleBody(it.articleList?.get(Random(Random(10).nextInt()).nextInt(it.articleList?.size!!))))
//                println(ArticleBodyParser.getArticleBody(it.articleList?.first()))
            }
        }

    }
}