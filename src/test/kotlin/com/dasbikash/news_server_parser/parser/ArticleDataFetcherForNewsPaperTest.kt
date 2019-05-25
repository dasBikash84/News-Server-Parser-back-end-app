package com.dasbikash.news_server_parser.parser

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class ArticleDataFetcherForNewsPaperTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    /*@Test
    fun readAllTopLevelPages() {

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val session = DbSessionManager.getNewSession()
        val query = session.createQuery(hql,Newspaper::class.java)
        val newsPapers = query.list() as List<Newspaper>

        session.close()

        var thread: Thread? = null

        *//*newsPapers.filter {
            true//it.id == NEWS_PAPER_ID.THE_GUARDIAN.id
        }
        .forEach {
            thread = Thread(ArticleDataFetcherForNewsPaper(it))
            thread?.start()
        }
        thread?.join()*//*
        //while (true)
        //return
    }*/
}