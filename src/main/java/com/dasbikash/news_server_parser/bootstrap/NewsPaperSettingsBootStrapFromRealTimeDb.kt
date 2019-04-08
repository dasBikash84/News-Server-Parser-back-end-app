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

import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.model.rest_end_points.RealTimeDbEndPoints
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.hibernate.Session
import java.io.File
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

object NewsPaperSettingsBootStrapFromRealTimeDb {

    var realTimeDbEndPoints:RealTimeDbEndPoints

    val jerseyClient = ClientBuilder.newClient()

    init {
        realTimeDbEndPoints =
                ObjectMapper()
                        .readValue(
                                File("src/main/resources/real_time_db_endpoints.json"),
                                RealTimeDbEndPoints::class.java
                        )
    }

    fun getDefaultSettingsData():DefaultAppSettings{
        val getResponse: Response =
                jerseyClient
                .target(realTimeDbEndPoints.rootSettingsNode)
                .request(MediaType.TEXT_PLAIN)
                .get()
        val responseText =  getResponse.readEntity(String::class.java)
        return Gson().fromJson(responseText,DefaultAppSettings::class.java)
    }

    fun saveDefaultSettings(session:Session):Boolean{
        val defaultAppSettings = getDefaultSettingsData()

        val languages:ArrayList<Language> = ArrayList()
        val countries:ArrayList<Country> = ArrayList()
        val newsPapers:ArrayList<Newspaper> = ArrayList()
        val pages:ArrayList<Page> = ArrayList()

        if(defaultAppSettings.languages !=null &&
            defaultAppSettings.countries !=null&&
            defaultAppSettings.newspapers !=null&&
            defaultAppSettings.pages !=null ){

            defaultAppSettings.languages!!.values.forEach { languageSetting ->
                languages.add(Language(languageSetting.id,languageSetting.name))
            }

            defaultAppSettings.countries!!.values.forEach { countrySetting ->
                countries.add(Country(countrySetting.name,countrySetting.countryCode,countrySetting.timeZone))
            }

            defaultAppSettings.newspapers!!.values.forEach { newsPaperSetting ->
                val newNewsPaper = Newspaper(id = newsPaperSetting.id,
                                                name = newsPaperSetting.name,
                                                active = newsPaperSetting.active)
                languages.filter {
                    it.id == newsPaperSetting.languageId
                }.map {
                    newNewsPaper.language = it
                }
                countries.filter {
                    it.name==newsPaperSetting.countryName
                }.map {
                    newNewsPaper.country = it
                }

                newsPapers.add(newNewsPaper)
            }

            defaultAppSettings.pages!!.values.forEach { pageSetting->
                val newPage = Page(id = pageSetting.id,parentPageId = pageSetting.parentPageId,name = pageSetting.name,
                                    linkFormat = pageSetting.linkFormat,linkVariablePartFormat = pageSetting.linkVariablePartFormat,
                                    active = pageSetting.active,firstEditionDateString = pageSetting.firstEditionDateString,
                                    weekly = pageSetting.weekly,weeklyPublicationDay = pageSetting.weeklyPublicationDay)
                newsPapers.filter {
                    it.id == pageSetting.newsPaperId
                }.map {
                    newPage.newspaper = it
                }

                pages.add(newPage)
            }

            val transaction = session.transaction
            try {
                transaction.begin()

                languages.forEach { session.save(it) }
                countries.forEach { session.save(it) }
                newsPapers.forEach { session.save(it) }
                pages.forEach { session.save(it) }

                transaction.commit()
            }catch (ex:Exception){
                transaction.rollback()
                return false
            }

        }else{
            return false
        }
        return true
    }

}