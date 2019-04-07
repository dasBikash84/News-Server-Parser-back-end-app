package com.dasbikash.news_server_parser.utils

import com.dasbikash.news_server_parser.model.DatabaseLog
import org.hibernate.Session

object LoggerUtils {

    fun consoleLog(message: String?) {
        println(message)
    }

    fun dbErrorLog(message: String,session: Session) {
        session.save(DatabaseLog(logMessage = "Error!!! "+message))
    }
}
