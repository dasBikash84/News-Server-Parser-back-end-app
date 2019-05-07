package com.dasbikash.news_server_parser.parser.article_body_parsers

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

internal class ArticleBodyParserTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    /*@Test
    fun readFirstPageArticles() {

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val session = DbSessionManager.getNewSession()
        val query = session.createQuery(hql,Newspaper::class.java)
        val newsPapers = query.list() as List<Newspaper>

        //session.close()

        newsPapers.filter {
            true//it.id == NEWS_PAPER_ID.THE_TIMES_OF_INDIA.id
        }
        .asSequence()
        .map {
            println("Newspaper: ${it.name}")
            Thread.sleep(500)
            //println("Page: ${it.pageList?.get(0)?.name}")
            it.pageList?.filter {
                it.hasData()
            }?.get(Random(Random(it.pageList!!.size).nextInt()).nextInt(it.pageList!!.size))
        }.map {
            println("Page: ${it?.name}")
            Thread.sleep(500)
            it?.let {
                val articleList = PreviewPageParser.parsePreviewPageForArticles(it, 1)
                it.articleList = articleList.first
                it.articleList
                        ?.forEach {
                            //session.persist(it)
                            println("Article  ${it}")
                        }
            }
            it
        }.map {
//            Thread.sleep(2000)
            it?.let {
                println("Article Data before parsing:" + it.articleList?.first())
                Thread.sleep(500)
                println("Article Data after parsing:" + ArticleBodyParser.getArticleBody(it.articleList?.get(Random(Random(10).nextInt()).nextInt(it.articleList?.size!!))))
//                println(ArticleBodyParser.getArticleBody(it.articleList?.first()))
            }
            Thread.sleep(2000)

        }
        .iterator().forEach {  }

    }*/
}