package com.dasbikash.news_server_parser.utils

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.io.File

object DbSessionManager {

    val CONFIG_FILE_PATH = "src/main/resources/hibernate.cfg.xml";

    val configuration:Configuration
    val sessionFactory: SessionFactory

    init {
        configuration = Configuration().configure(File(CONFIG_FILE_PATH))
        sessionFactory = configuration.buildSessionFactory()
    }

    fun getNewSession():Session{
        return sessionFactory.openSession()
    }
}