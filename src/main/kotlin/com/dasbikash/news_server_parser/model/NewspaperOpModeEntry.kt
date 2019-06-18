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

import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = DatabaseTableNames.NEWS_PAPER_OP_MODE_ENTRY_NAME)
data class NewspaperOpModeEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @Column(columnDefinition = "enum('RUNNING','GET_SYNCED','PARSE_THROUGH_CLIENT')")
        @Enumerated(EnumType.STRING)
        var opMode: ParserMode? = null,

        @ManyToOne(targetEntity = Newspaper::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "newsPaperId")
        var newspaper: Newspaper? = null,
        @UpdateTimestamp
        var created: Date? = null
){
        companion object{
                private val DEFAULT_PARSER_MODE = ParserMode.RUNNING

                fun getDefaultEntryForNewspaper(newspaper: Newspaper):NewspaperOpModeEntry{
                        return NewspaperOpModeEntry(opMode = DEFAULT_PARSER_MODE,newspaper = newspaper)
                }
        }

        override fun toString(): String {
                return "NewspaperOpModeEntry(opMode=$opMode, newspaper=${newspaper!!.name}, created=$created)"
        }

}