package com.dasbikash.news_server_parser.bootstrap

import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.model.rest_end_points.RealTimeDbEndPoints
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.hibernate.Session
import java.io.File
import java.lang.Exception
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

object NewsPaperSettingsBootStrapFromRealTimeDb {

    lateinit var realTimeDbEndPoints:RealTimeDbEndPoints

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

    private class Countries{
        var countries:HashMap<String,CountrySetting>? = null
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
                val newPage = Page(id = pageSetting.id,parentPageId = pageSetting.id,name = pageSetting.name,
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