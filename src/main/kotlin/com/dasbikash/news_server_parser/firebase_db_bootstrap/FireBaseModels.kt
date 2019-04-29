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

package com.dasbikash.news_server_parser.firebase_db_bootstrap

import com.dasbikash.news_server_parser.model.*
import java.util.*
import kotlin.collections.ArrayList


class LanguageForFS(
        val id:String,
        var name: String){
    companion object{
        fun getFromLanguage(language: Language)
                =LanguageForFS(language.id,language.name!!)
    }
}

class CountryForFS(
        val name: String,
        val countryCode: String,
        val timeZone: String){
    companion object{
        fun getFromCountry(country: Country)
                =CountryForFS(country.name,country.countryCode!!,country.timeZone!!)
    }
}
class NewspaperForFS(
        val id: String,
        val name: String,
        val countryName: String,
        val languageId: String,
        val active: Boolean){
    companion object{
        fun getFromNewspaper(newspaper: Newspaper) =
                NewspaperForFS(newspaper.id,newspaper.name!!,newspaper.country!!.name,
                        newspaper.language!!.id,newspaper.active)
    }
}

class PageForFS(
        val id: String,
        val newspaperId: String,
        val parentPageId: String,
        val name: String,
        val linkFormat:String?,
        val linkVariablePartFormat:String?,
        val firstEditionDateString:String?,
        val weekly:Boolean?,
        val weeklyPublicationDay:Int?,
        val active: Boolean
){
    companion object{
        fun getFromPage(page: Page) =
                PageForFS(page.id,page.newspaper!!.id,page.parentPageId!!,page.name!!,
                            page.linkFormat,page.linkVariablePartFormat,page.firstEditionDateString,
                            page.weekly,page.weeklyPublicationDay,page.active)
    }

}
class ArticleForFs(
        var id: String="",
        var pageId: String="",
        var title: String="",
        val publicationTime: Date? = null,
        private var publicationTimeRTDB: Long? = null,
        var articleText: String="",
        var imageLinkList: List<ArticleImage> = ArrayList(),
        var previewImageLink: String? =""
){
    companion object{
        fun fromArticle(article: Article):ArticleForFs{

            if(article.previewImageLink == null && article.imageLinkList.size>0){
                article.previewImageLink = article.imageLinkList.first().link
            }

            var publicationTime:Date? = null
            when{
                article.publicationTS !=null -> publicationTime = article.publicationTS
                article.modificationTS !=null -> publicationTime = article.modificationTS
                else -> publicationTime = article.modified
            }
            return ArticleForFs(article.id,article.page!!.id,article.title!!,publicationTime,null,
                                article.articleText!!,article.imageLinkList,article.previewImageLink)
        }
    }

    fun getPublicationTimeRTDB():Long{
        return publicationTime?.time ?: 0L
    }

    fun setPublicationTimeRTDB(pubTime:Long){
        publicationTimeRTDB = pubTime
    }

    override fun toString(): String {
        return "ArticleForFs(id='$id', title='$title', publicationTimeRTDB=${publicationTimeRTDB})"
    }

}

