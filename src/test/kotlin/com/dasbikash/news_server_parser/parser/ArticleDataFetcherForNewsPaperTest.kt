package com.dasbikash.news_server_parser.parser

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.ParserMode
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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

//    @Test
//    fun testRun(){
//        val session = DbSessionManager.getNewSession()
//        val newsPaper = DatabaseUtils.getAllNewspapers(session).find { it.id=="NP_ID_6" }
//        newsPaper?.let {
//            session.detach(it)
//            val articleDataFetcherThroughClient = ArticleDataFetcherSelf.getInstanceForRunning(it)
//            articleDataFetcherThroughClient.start()
//            articleDataFetcherThroughClient.join()
//        }
//    }
}