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

package com.dasbikash.news_server_parser.bootstrap

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.utils.FileReaderUtils

object BootstrapSettings {

    @JvmStatic
    fun main(args: Array<String>) {
//        loadSettingsFromLocalJson()
//        displayAll()
        loadPageGroupData()
    }

    fun loadPageGroupData() {
        val session = DbSessionManager.getNewSession()
        val pages= DatabaseUtils.getAllPages(session)

        session.beginTransaction()

        FileReaderUtils.jsonFileToEntityList("/page_group_data.json", PageGroups::class.java)
                .pageGroups!!.asSequence().forEach {
            it.setPages(pages)
            session.save(it)
        }
        session.transaction.commit()
        session.close()
        displayAll()
    }

    private fun loadSettingsFromLocalJson() {
        val session = DbSessionManager.getNewSession()
        session.beginTransaction()
        val languages = FileReaderUtils.jsonFileToEntityList("/language_data.json", Languages::class.java)
                .languages!!
        languages.asSequence().forEach { session.save(it) }

        val countries = FileReaderUtils.jsonFileToEntityList("/country_data.json", Countries::class.java)
                .countries!!
        countries.asSequence().forEach { session.save(it) }

        val newspapers = FileReaderUtils.jsonFileToEntityList("/newspaper_data.json", Newspapers::class.java)
                .newspapers!!

        newspapers.asSequence().map {
            it.setCountryData(countries)
            it.setlanguageData(languages)
            it
        }.forEach { session.save(it) }

        val pages = FileReaderUtils.jsonFileToEntityList("/page_data_full.json", Pages::class.java).pages!!

        pages.asSequence().forEach {
            it.setNewspaper(newspapers)
            session.save(it)
        }

        FileReaderUtils.jsonFileToEntityList("/page_group_data.json", PageGroups::class.java)
                .pageGroups!!.asSequence().forEach {
            it.setPages(pages)
            session.save(it)
        }
        session.transaction.commit()
        session.close()
    }

    private fun displayAll() {
        val session = DbSessionManager.getNewSession()
        DatabaseUtils.getAllCountries(session).asSequence().forEach { println(it) }
        DatabaseUtils.getAllLanguages(session).asSequence().forEach { println(it) }
        DatabaseUtils.getAllNewspapers(session).asSequence().forEach { println(it) }
        DatabaseUtils.getAllPages(session).asSequence().forEach { println(it) }
        DatabaseUtils.getAllPageGroups(session).asSequence().forEach { println(it) }
        session.close()
    }
}


