package com.dasbikash.news_server_parser.parser.preview_page_parser

import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.io.File

internal class ArticleDataFeatcherForNewsPaperTest {


    lateinit var configuration:Configuration
    lateinit var sessionFactory:SessionFactory
    lateinit var session: Session


    @BeforeEach
    fun setUp() {
        configuration = Configuration().configure(File("src/main/resources/hibernate.cfg.xml"))
        sessionFactory = configuration.buildSessionFactory()
        session = sessionFactory.openSession()
    }

    @AfterEach
    fun tearDown() {
//        session.close()
    }

    @Test
    fun run() {
        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val query = session.createQuery(hql)
//        .forEach { childPageList.add(it as Page) }
        val newsPapers= query.list() as List<Newspaper>
        //session.delete(newsPapers)
        session.close()
//        Thread.sleep(1000)
        println()
        println()
        println()
        val thList = mutableListOf<Thread>()
        newsPapers.filter { true }.forEach {
            println(it.name)
            val thread = Thread(ArticleDataFeatcherForNewsPaper(it))
            thread.start()
            thList.add(thread)
            //thread.join()
        }

        thList.forEach { it.join() }

        //Thread.sleep(10000)


    }
}