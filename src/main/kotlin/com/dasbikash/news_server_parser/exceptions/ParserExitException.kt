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

package com.dasbikash.news_server_parser.exceptions

import com.dasbikash.news_server_parser.exceptions.generic.HighestLevelException
import com.dasbikash.news_server_parser.model.Newspaper

class ParserExitException: HighestLevelException {

    constructor(newspaper: Newspaper,ex:Throwable) : super(causePreamble+"${newspaper.name} with id: ${newspaper.id}",ex)
    constructor() : super()

    companion object {
        val causePreamble = "Parser Exit Exception for Newspaper: ";
    }
}