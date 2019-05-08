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

import com.dasbikash.news_server_parser.bootstrap.Countries
import com.dasbikash.news_server_parser.bootstrap.Languages
import javax.persistence.*

@Entity
@Table(name = DatabaseTableNames.NEWSPAPER_TABLE_NAME)
data class Newspaper(
        @Id var id: String = "",
        var name: String? = null,

        @ManyToOne(targetEntity = Country::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "countryName")
        var country: Country? = null,

        @ManyToOne(targetEntity = Language::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "languageId")
        var language: Language? = null,

        var active: Boolean = true,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "newspaper", targetEntity = Page::class)
        var pageList: List<Page>? = null


) {
    @Transient
    var countryName:String?=null
    @Transient
    var languageId:String?=null

    override fun toString(): String {
        return "Newspaper(id='$id', name=$name, country=${country?.name}, language=${language?.name}, active=$active)"
    }
    fun setCountryData(countries: List<Country>){
        countries.asSequence().forEach {
            if (it.name == countryName){
                country = it
                return@forEach
            }
        }
    }
    fun setlanguageData(languages: List<Language>){
        languages.asSequence().forEach {
            if (it.id == languageId){
                language = it
                return@forEach
            }
        }
    }
}