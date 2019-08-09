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

package com.dasbikash.news_server_parser.firebase

import com.dasbikash.news_server_parser.database.DatabaseUtils
import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.utils.EmailUtils
import com.dasbikash.news_server_parser.utils.LoggerUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


object RealTimeDbAdminTaskUtils {

    private const val TOKEN_GENERATION_REQUEST_NODE = "parser_token_generation_request"
    private const val NP_STATUS_CHANGE_REQUEST_NODE = "np_status_change_request"
    private const val NP_PARSER_MODE_CHANGE_REQUEST_NODE = "np_parser_mode_change_request"

    init {
        RealTimeDbRefUtils.getAdminTaskDataNode()
                .child(TOKEN_GENERATION_REQUEST_NODE)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {}

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        snapshot?.let {
                            val session = DbSessionManager.getNewSession()
                            it.children.asSequence().forEach {
                                val tokenGenerationRequest = it.getValue(TokenGenerationRequest::class.java)
                                if (tokenGenerationRequest.isValid()) {
                                    val token = AuthToken()
                                    DatabaseUtils.runDbTransection(session) { session.save(token) }
                                    EmailUtils.emailAuthTokenToAdmin(token)
                                    LoggerUtils.logOnDb("New auth token generated.", session)
                                }
                                deleteTokenGenerationRequest(it.ref.key)
                            }
                            session.close()
                        }

                    }
                })

        RealTimeDbRefUtils.getAdminTaskDataNode()
                .child(NP_STATUS_CHANGE_REQUEST_NODE)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {}

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        snapshot?.let {

                            val session = DbSessionManager.getNewSession()

                            it.children.asSequence().forEach {

                                val newsPaperStatusChangeRequest =
                                        it.getValue(NewsPaperStatusChangeRequest::class.java)

                                val token = session.get(AuthToken::class.java, newsPaperStatusChangeRequest.authToken)

                                if (token != null && !token.hasExpired()) {
                                    val newspaper = session.get(Newspaper::class.java, newsPaperStatusChangeRequest.targetNewspaperId)
                                    if (newspaper != null) {
                                        if (newsPaperStatusChangeRequest.isOnRequest() && !newspaper.active) {
                                            newspaper.pageList?.filter { it.hasData() && it.isPaginated() }?.asSequence()?.forEach {
                                                val pageParsingHistory =
                                                        PageParsingHistory.getEmptyParsingHistoryForPage(it)
                                                DatabaseUtils.runDbTransection(session) { session.save(pageParsingHistory) }
                                            }
                                            newspaper.active = true
                                            DatabaseUtils.runDbTransection(session) { session.update(newspaper) }
                                            LoggerUtils.logOnDb("Np ${newspaper.name} activated", session)
                                        } else if (newsPaperStatusChangeRequest.isOffRequest() && newspaper.active) {
                                            newspaper.active = false
                                            DatabaseUtils.runDbTransection(session) { session.update(newspaper) }
                                            LoggerUtils.logOnDb("Np ${newspaper.name} deactivated", session)
                                        }
                                    }
                                    token.makeExpired()
                                    DatabaseUtils.runDbTransection(session) { session.update(token) }
                                }

                                deleteNewsPaperStatusChangeRequest(it.ref.key)
                            }

                            session.close()
                        }

                    }
                })

        RealTimeDbRefUtils.getAdminTaskDataNode()
                .child(NP_PARSER_MODE_CHANGE_REQUEST_NODE)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {}

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        snapshot?.let {

                            val session = DbSessionManager.getNewSession()

                            it.children.asSequence().forEach {

                                val newsPaperParserModeChangeRequest =
                                        it.getValue(NewsPaperParserModeChangeRequest::class.java)

                                val token = session.get(AuthToken::class.java, newsPaperParserModeChangeRequest.authToken)

                                if (token != null && !token.hasExpired()) {
                                    val newspaper = session.get(Newspaper::class.java, newsPaperParserModeChangeRequest.targetNewspaperId)
                                    if (newspaper != null) {
                                        if (newsPaperParserModeChangeRequest.isRunningRequest()) {
                                            val npOpModeEntry = NewspaperOpModeEntry(opMode = ParserMode.RUNNING,newspaper = newspaper)
                                            DatabaseUtils.runDbTransection(session) { session.save(npOpModeEntry) }
                                            LoggerUtils.logOnDb("Operation mode set to ${ParserMode.RUNNING} for Np ${newspaper.name}.", session)
                                        } else if (newsPaperParserModeChangeRequest.isGetSyncedRequest()) {
                                            val npOpModeEntry = NewspaperOpModeEntry(opMode = ParserMode.GET_SYNCED,newspaper = newspaper)
                                            DatabaseUtils.runDbTransection(session) { session.save(npOpModeEntry) }
                                            LoggerUtils.logOnDb("Operation mode set to ${ParserMode.GET_SYNCED} for Np ${newspaper.name}.", session)
                                        } else if (newsPaperParserModeChangeRequest.isParseThroughClientRequest()) {
                                            val npOpModeEntry = NewspaperOpModeEntry(opMode = ParserMode.PARSE_THROUGH_CLIENT,newspaper = newspaper)
                                            DatabaseUtils.runDbTransection(session) { session.save(npOpModeEntry) }
                                            LoggerUtils.logOnDb("Operation mode set to ${ParserMode.PARSE_THROUGH_CLIENT} for Np ${newspaper.name}.", session)
                                        }
                                    }
                                    token.makeExpired()
                                    DatabaseUtils.runDbTransection(session) { session.update(token) }
                                }

                                deleteNewsPaperParserModeChangeRequest(it.ref.key)
                            }

                            session.close()
                        }

                    }
                })
    }

    fun init() {}

    private fun deleteTokenGenerationRequest(nodeId: String) {
        RealTimeDbRefUtils.getAdminTaskDataNode()
                .child(TOKEN_GENERATION_REQUEST_NODE)
                .child(nodeId)
                .setValueAsync(null)
    }

    private fun deleteNewsPaperStatusChangeRequest(nodeId: String) {
        RealTimeDbRefUtils.getAdminTaskDataNode()
                .child(NP_STATUS_CHANGE_REQUEST_NODE)
                .child(nodeId)
                .setValueAsync(null)
    }

    private fun deleteNewsPaperParserModeChangeRequest(nodeId: String) {
        RealTimeDbRefUtils.getAdminTaskDataNode()
                .child(NP_PARSER_MODE_CHANGE_REQUEST_NODE)
                .child(nodeId)
                .setValueAsync(null)
    }
}