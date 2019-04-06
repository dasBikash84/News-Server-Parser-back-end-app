package com.dasbikash.news_server_parser.parser.preview_page_parsers.anando_bazar

import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.parser.NEWS_PAPER_ID
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParseRequest
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParserFactory
import com.dasbikash.news_server_parser.parser.preview_page_parsers.the_gurdian.TheGurdianPreviewPageParser
import com.dasbikash.news_server_parser.utils.DbSessionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AnandoBazarPreviewPageParserTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun readFirstPageArticles(){

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val query = DbSessionManager.getNewSession().createQuery(hql)
        val newsPapers= query.list() as List<Newspaper>

        newsPapers.filter {
            it.id == NEWS_PAPER_ID.ANANDO_BAZAR.id
        }.map {
            println("Np: ${it?.name}")
            println("Page: ${it.pageList?.get(0)?.name}")
            it.pageList?.filter {
                it.linkFormat !=null
            }?.first()
        }.map {
            it?.let {
                PreviewPageParser.parsePreviewPage(it,1)
                        ?.forEach {
                            println("Article  ${it}")
                        }
            }
        }

    }
}