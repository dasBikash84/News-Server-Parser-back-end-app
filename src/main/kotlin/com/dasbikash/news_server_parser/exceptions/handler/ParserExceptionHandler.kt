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

package com.dasbikash.news_server_parser.exceptions.handler

import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.exceptions.HighestLevelException
import com.dasbikash.news_server_parser.exceptions.LowLevelException
import com.dasbikash.news_server_parser.exceptions.MediumLevelException
import com.dasbikash.news_server_parser.exceptions.ParserException
import com.dasbikash.news_server_parser.model.ErrorLog
import com.dasbikash.news_server_parser.utils.EmailUtils
import com.dasbikash.news_server_parser.utils.LoggerUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

object ParserExceptionHandler {

    fun handleException(ex: ParserException) {
        Observable.just(ex)
                .subscribeOn(Schedulers.io())
                .map {
                    when (it) {
                        is HighestLevelException    -> highestLevelExceptionHandler(it)
                        is MediumLevelException     -> mediumLevelExceptionHandler(it)
                        is LowLevelException        -> lowLevelExceptionHandler(it)
                        else                        -> exceptionHandler(it)
                    }
                }
                .doOnError {
                    handleException(ParserException(it))
                }
                .subscribe()
    }

    private fun exceptionHandler(ex: ParserException) {
        val session = DbSessionManager.getNewSession()
        LoggerUtils.logError(ex, session)
        session.close()
    }

    private fun lowLevelExceptionHandler(ex: ParserException) {
        exceptionHandler(ex)
    }

    private fun mediumLevelExceptionHandler(ex: MediumLevelException) {
        exceptionHandler(ex)
    }

    private fun highestLevelExceptionHandler(ex: HighestLevelException) {
        EmailUtils.sendEmail("Major error on News-Server parser App!!! Cause: ${ex.message
                                ?: ex.cause}", ErrorLog(ex).toString())
        exceptionHandler(ex)
    }

}