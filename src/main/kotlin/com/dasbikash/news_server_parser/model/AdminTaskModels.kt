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

enum class OffOnStatus {
    ON,OFF
}

data class NewsPaperParserModeChangeRequest (
        var authToken: String="",
        var targetNewspaperId: String="",
        var parserMode: String = ""
){
    fun isRunningRequest():Boolean{
        return parserMode.equals(ParserMode.RUNNING.name)
    }
    fun isGetSyncedRequest():Boolean{
        return parserMode.equals(ParserMode.GET_SYNCED.name)
    }
    fun isParseThroughClientRequest():Boolean{
        return parserMode.equals(ParserMode.PARSE_THROUGH_CLIENT.name)
    }
    fun isOffRequest():Boolean{
        return parserMode.equals(ParserMode.OFF.name)
    }
    fun hasValidMode():Boolean{
        return parserMode.equals(ParserMode.OFF.name) ||
                parserMode.equals(ParserMode.GET_SYNCED.name) ||
                parserMode.equals(ParserMode.PARSE_THROUGH_CLIENT.name) ||
                parserMode.equals(ParserMode.RUNNING.name)
    }
}

data class NewsPaperStatusChangeRequest(
        var authToken: String = "",
        var targetNewspaperId: String="",
        var targetStatus: String=""
){
    fun isOnRequest():Boolean{
        return targetStatus.equals(OffOnStatus.ON.name)
    }
    fun isOffRequest():Boolean{
        return targetStatus.equals(OffOnStatus.OFF.name)
    }
}