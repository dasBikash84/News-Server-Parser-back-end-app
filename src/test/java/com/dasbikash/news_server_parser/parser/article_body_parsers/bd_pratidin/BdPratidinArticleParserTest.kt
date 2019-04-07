package com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin

import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.parser.NEWS_PAPER_ID
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.DbSessionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class BdPratidinArticleParserTest {

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
        val query = session.createQuery(hql)
        val newsPapers= query.list() as List<Newspaper>

        //session.close()

        newsPapers.filter {
            it.id == NEWS_PAPER_ID.BANGLADESH_PROTIDIN.id
        }.map {
            println("Np: ${it?.name}")
            //println("Page: ${it.pageList?.get(0)?.name}")
            it.pageList?.filter {
                it.linkFormat !=null
            }?.get(Random(Random(it.pageList!!.size).nextInt()).nextInt(it.pageList!!.size))
        }.map {
            it?.let {
                val articleList = PreviewPageParser.parsePreviewPage(it,1)
                it.articleList = articleList
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
                println("Article Data after parsing:"+ ArticleBodyParser.loadArticleBody(it.articleList?.get(Random(Random(10).nextInt()).nextInt(it.articleList?.size!!))))
//                println(ArticleBodyParser.loadArticleBody(it.articleList?.first()))
            }
        }

    }
}