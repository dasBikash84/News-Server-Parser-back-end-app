@file:JvmName("DataParserInitiator")

/*
 * Copyright 2019 das.bikash.dev@gmail.com. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.dasbikash.news_server_parser

import com.dasbikash.news_server_parser.bootstrap.NewsPaperSettingsBootStrapFromRealTimeDb
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.parser.ArticleDataFeatcherForNewsPaper


object DataParserInitiator {

    @JvmStatic
    fun main(args: Array<String>) {

        val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
        val session = DbSessionManager.getNewSession()
        var newsPapers: List<Newspaper>

        do {
            val query = session.createQuery(hql, Newspaper::class.java)
            newsPapers = query.list() as List<Newspaper>
            if (newsPapers.size == 0) {
                println("#################################################################")
                println("Settings data not found, so going to load from remote server.....")
                println("#################################################################")
                NewsPaperSettingsBootStrapFromRealTimeDb.saveDefaultSettings(session)
                continue
            }
            break
        } while (true)

        session.close()
        Thread.sleep(1000)

        val threadPool = mutableListOf<Thread>()

        newsPapers.asSequence()
                .forEach {
                    val thread = Thread(ArticleDataFeatcherForNewsPaper(it))
                    thread.start()
                    threadPool.add(thread)
                }

        threadPool.forEach { it.join() }
    }
}
