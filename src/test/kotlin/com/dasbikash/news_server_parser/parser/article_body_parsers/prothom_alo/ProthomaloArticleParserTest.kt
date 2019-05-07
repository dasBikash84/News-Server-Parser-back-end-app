package com.dasbikash.news_server_parser.parser.article_body_parsers.prothom_alo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

internal class ProthomaloArticleParserTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    /*@Test
    fun readFirstPageArticles(){

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val  session = DbSessionManager.getNewSession()
        val query = session.createQuery(hql,Newspaper::class.java)
        val newsPapers= query.list() as List<Newspaper>

        //session.close()

        newsPapers.filter {
            it.id == NEWS_PAPER_ID.PROTHOM_ALO.id
        }.map {
            println("Np: ${it.name}")
            //println("Page: ${it.pageList?.get(0)?.name}")
            it.pageList?.filter {
                it.hasData()
            }?.get(Random(Random(it.pageList!!.size).nextInt()).nextInt(it.pageList!!.size))
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

    }*/
}