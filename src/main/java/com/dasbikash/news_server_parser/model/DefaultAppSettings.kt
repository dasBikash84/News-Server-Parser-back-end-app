/*
 * Copyright 2019 www.dasbikash.org. All rights reserved.
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

import java.util.*

class DefaultAppSettings {

    var countries: HashMap<String, CountrySetting>? = null
    var languages: HashMap<String, LanguageSetting>? = null
    var newspapers: HashMap<String, NewspaperSetting>? = null
    var pages: HashMap<String, PageSetting>? = null
    var page_groups: HashMap<String, PageGroupSetting>? = null
    var update_time: HashMap<String, Long>? = null

    override fun toString(): String {
        return "DefaultAppSettings(countries=$countries, languages=$languages, newspapers=$newspapers, pages=$pages, page_groups=$page_groups, update_time=$update_time)"
    }

}

data class LanguageSetting (var id:String="",var name: String?=null)

data class CountrySetting (var name: String="",var countryCode: String?=null,var timeZone: String?=null)

data class NewspaperSetting(var id: String="",var name: String?=null,var countryName: String?=null,
                                var languageId: String?=null,var active: Boolean=true)

data class PageSetting(var id: String="",var newsPaperId: String?=null,var parentPageId: String?=null,
                           var name: String?=null,var linkFormat:String? = null,var linkVariablePartFormat:String? = "page_num",
                           var firstEditionDateString:String? = null,var weekly:Boolean = false,var weeklyPublicationDay:Int? = 0,
                           var active: Boolean = true)

data class PageGroupSetting(var id: String = "",var name: String? = null,
                            var active: Boolean = true,var pageList: List<String>? = null)