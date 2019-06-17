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

package com.dasbikash.news_server_parser.model

import com.dasbikash.news_server_parser.utils.LoggerUtils
import java.util.*
import javax.persistence.*


@Entity
@Table(name = DatabaseTableNames.PAGE_PARSING_INTERVAL_TABLE_NAME)
data class PageParsingInterval(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,

        @ManyToOne(targetEntity = Page::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "pageId")
        var page: Page? = null,
        var parsingIntervalMS: Int? = null,
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "modified", nullable = false, updatable=false,insertable = false)
        var modified: Date? = null
) {
    override fun toString(): String {
        return "PageParsingInterval(id=$id, Np=${page?.newspaper?.name},page=${page?.id}, pageName=${page?.name}, parsingIntervalMin=${parsingIntervalMS!!/ 60/1000}, modified=$modified)"
    }
    fun needRecalculation():Boolean{
        return (System.currentTimeMillis() - this.modified!!.time)>MINIMUM_RECALCULATE_INTERVAL
    }

    companion object{
        const val MINIMUM_RECALCULATE_INTERVAL = 24*60*60*1000L //1 Day
        const val SUCCESIVE_ARTICLE_PARSING_INTERVAL = 1*60*1000L //1 min
        const val WEEKLY_ARTICLE_PARSING_INTERVAL = 60*60*1000L //1hr
        const val MAX_ARTICLE_PARSING_INTERVAL = 6*60*60*1000L //6hr
        const val MIN_ARTICLE_PARSING_INTERVAL = 30*60*1000L //30 min

        private fun getInstanceForPage(page: Page,parsingIntervalMS: Int):
                PageParsingInterval{
            return PageParsingInterval(page = page,parsingIntervalMS = parsingIntervalMS)
        }

        fun recalculate(page: Page):PageParsingInterval {
            if (page.weekly){
                return getInstanceForPage(page,WEEKLY_ARTICLE_PARSING_INTERVAL.toInt())
            }else if (page.articleList!!.isEmpty() || page.articleList!!.size==1){
                return getInstanceForPage(page,MAX_ARTICLE_PARSING_INTERVAL.toInt())
            }else{
                val sortedArticles = page.articleList!!.map {
                    it
                }.sortedBy { it.modified }.toList()
                var totalParsingIntervalMs = 0.0f
                var totalItemsConsidered = 0
                for (index in sortedArticles.indices){
                    if (index == 0){
                        continue
                    }else if (sortedArticles[index].modified!!.time - sortedArticles[index-1].modified!!.time > SUCCESIVE_ARTICLE_PARSING_INTERVAL){
                        totalParsingIntervalMs += (sortedArticles[index].modified!!.time - sortedArticles[index-1].modified!!.time).toFloat()
                        totalItemsConsidered +=1
                    }
                }
                if (totalItemsConsidered == 0){
                    return getInstanceForPage(page,MAX_ARTICLE_PARSING_INTERVAL.toInt())
                }else{
                    val parsingIntervalMS = (totalParsingIntervalMs/totalItemsConsidered/4).toInt()

                    if (parsingIntervalMS > MAX_ARTICLE_PARSING_INTERVAL){
                        return getInstanceForPage(page, MAX_ARTICLE_PARSING_INTERVAL.toInt())
                    }else if(parsingIntervalMS < MIN_ARTICLE_PARSING_INTERVAL) {
                        return getInstanceForPage(page, MIN_ARTICLE_PARSING_INTERVAL.toInt())
                    }else {
                        return getInstanceForPage(page, parsingIntervalMS)
                    }
                }
            }
        }
    }

}