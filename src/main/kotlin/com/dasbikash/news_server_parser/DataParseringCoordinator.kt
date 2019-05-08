@file:JvmName("com.dasbikash.news_server_parser.DataParseringCoordinator")

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

import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.parser.ArticleDataFeatcherForNewsPaper


enum class ParserMode {
    RUNNING, GET_SYNCED
}

object DataParseringCoordinator {

    private val opMode = ParserMode.RUNNING

    private val ITERATION_DELAY = 15 * 60 * 1000L //15 mins

    private val articleDataFetcherMap
            = mutableMapOf<String, ArticleDataFeatcherForNewsPaper>()


    @JvmStatic
    fun main(args: Array<String>) {
        do {
            try {
                val session = DbSessionManager.getNewSession()
                val hql = "FROM ${EntityClassNames.NEWSPAPER} where active=true"
                val query = session.createQuery(hql, Newspaper::class.java)
                val newsPapers = query.list() as List<Newspaper>

                newsPapers.asSequence()
                        .forEach {
                            if (articleDataFetcherMap.containsKey(it.id) && !articleDataFetcherMap.get(it.id)!!.isAlive) {
                                articleDataFetcherMap.remove(it.id)
                            }
                            if (!articleDataFetcherMap.containsKey(it.id)) {
                                session.detach(it)
                                val articleDataFeatcherForNewsPaper = ArticleDataFeatcherForNewsPaper(it, opMode)
                                articleDataFetcherMap.put(it.id, articleDataFeatcherForNewsPaper)
                                articleDataFeatcherForNewsPaper.start()
                            }
                        }
                session.close()
                Thread.sleep(ITERATION_DELAY)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
                handleException(ex)
            }
        } while (true)
    }

    private fun handleException(ex: InterruptedException) {

    }
}
