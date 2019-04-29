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

import javax.persistence.*

@Entity
@Table(name = DatabaseTableNames.PAGE_TABLE_NAME)
data class Page(
        @Id
        var id: String="",

        @ManyToOne(targetEntity = Newspaper::class,fetch = FetchType.EAGER)
        @JoinColumn(name="newsPaperId")
        var newspaper: Newspaper?=null,

        var parentPageId: String?=null,
        var name: String?=null,

        @Column(name="linkFormat", columnDefinition="text")
        var linkFormat:String? = null,

        var linkVariablePartFormat:String? = DEFAULT_LINK_TRAILING_FORMAT,
        var firstEditionDateString:String? = null,
        var weekly:Boolean = false,
        var weeklyPublicationDay:Int? = 0,
        var active: Boolean = true,

        @OneToMany(fetch = FetchType.LAZY,mappedBy = "page",targetEntity = Article::class)
        var articleList: List<Article>?=null,

        @OneToMany(fetch = FetchType.LAZY,mappedBy = "page",targetEntity = PageParsingHistory::class)
        var pageParsingHistory: List<PageParsingHistory>?=null

){
    companion object {
        @JvmField
        val TOP_LEVEL_PAGE_PARENT_ID = "PAGE_ID_0"
        @JvmField
        val DEFAULT_LINK_TRAILING_FORMAT = "page_num"
    }

    @Transient
    fun isTopLevelPage():Boolean{
        return parentPageId == TOP_LEVEL_PAGE_PARENT_ID
    }
    @Transient
    fun isPaginated():Boolean{
        return linkVariablePartFormat !=null
    }
    @Transient
    fun hasData():Boolean{
        return linkFormat != null
    }

    override fun toString(): String {
        return "Page(id='$id', newspaper=$newspaper, parentPageId=$parentPageId, name=$name, linkFormat=$linkFormat, linkVariablePartFormat=$linkVariablePartFormat, firstEditionDateString=$firstEditionDateString, weekly=$weekly, weeklyPublicationDay=$weeklyPublicationDay, active=$active)"
    }

}

//is_weekly,weekly_pub_day,link_format,link_variable_part_format,first_edition_date_string