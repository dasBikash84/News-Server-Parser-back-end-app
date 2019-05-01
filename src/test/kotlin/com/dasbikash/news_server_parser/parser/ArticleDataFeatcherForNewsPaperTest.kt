package com.dasbikash.news_server_parser.parser

import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ArticleDataFeatcherForNewsPaperTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun readAllTopLevelPages() {

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val session = DbSessionManager.getNewSession()
        val query = session.createQuery(hql,Newspaper::class.java)
        val newsPapers = query.list() as List<Newspaper>

        session.close()

        var thread: Thread? = null

        /*newsPapers.filter {
            true//it.id == NEWS_PAPER_ID.THE_GUARDIAN.id
        }
        .forEach {
            thread = Thread(ArticleDataFeatcherForNewsPaper(it))
            thread?.start()
        }
        thread?.join()*/
        //while (true)
        //return
    }
}