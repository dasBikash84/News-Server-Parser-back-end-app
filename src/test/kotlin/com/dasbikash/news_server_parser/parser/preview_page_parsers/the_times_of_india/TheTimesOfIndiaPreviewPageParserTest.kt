package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_times_of_india

import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.parser.NEWS_PAPER_ID
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TheTimesOfIndiaPreviewPageParserTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun readFirstPageArticles() {

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val query = DbSessionManager.getNewSession().createQuery(hql,Newspaper::class.java)
        val newsPapers = query.list() as List<Newspaper>

        newsPapers.filter {
            it.id == NEWS_PAPER_ID.THE_TIMES_OF_INDIA.id
        }.map {
            println("Np: ${it.name}")
            println("Page: ${it.pageList?.get(0)?.name}")
            it.pageList?.filter {
                it.hasData()
            }?.first()
        }.map {
            it?.let {
                PreviewPageParser.parsePreviewPageForArticles(it, 1).first
                        ?.forEach {
                            println("Article  ${it}")
                        }
            }
        }

    }
}