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

package com.dasbikash.news_server_parser.parser

object NewsPaperDetails {
    val THE_GUARDIAN_ID = "NP_ID_1"
    val PROTHOM_ALO_ID = "NP_ID_2"
    val ANANDO_BAZAR_ID = "NP_ID_3"
    val THE_DAILY_STAR_ID = "NP_ID_4"
    val THE_INDIAN_EXPRESS_ID = "NP_ID_5"
    val DOINIK_ITTEFAQ_ID = "NP_ID_6"
    val THE_TIMES_OF_INDIA_ID = "NP_ID_7"
    val DAILY_MIRROR_ID = "NP_ID_8"
    val DHAKA_TRIBUNE_ID = "NP_ID_9"
    val BANGLADESH_PROTIDIN_ID = "NP_ID_10"
    val DAWN_ID = "NP_ID_11"
    val KALER_KONTHO_ID = "NP_ID_12"
    val JUGANTOR_ID = "NP_ID_13"
    val THE_FINANCIAL_EXPRESS_ID = "NP_ID_14"
    val BONIK_BARTA_ID = "NP_ID_15"
    val BHORER_KAGOJ_ID = "NP_ID_16"
    val NEW_AGE_ID = "NP_ID_17"
    val DAILY_SUN_ID = "NP_ID_18"
    val JAI_JAI_DIN_ID = "NP_ID_19"
    val AMADER_SOMOY_ID = "NP_ID_20"
    val DAILY_OBSERVER_ID = "NP_ID_21"
    val BANGLA_NEWS_24_BN_ID = "NP_ID_22"
    val BANGLA_NEWS_24_EN_ID = "NP_ID_23"
    val MANOB_ZAMIN_ID = "NP_ID_24"
}

enum class NEWS_PAPER_ID private constructor(val id: String) {
    THE_GUARDIAN(NewsPaperDetails.THE_GUARDIAN_ID),
    PROTHOM_ALO(NewsPaperDetails.PROTHOM_ALO_ID),
    ANANDO_BAZAR(NewsPaperDetails.ANANDO_BAZAR_ID),
    THE_DAILY_STAR(NewsPaperDetails.THE_DAILY_STAR_ID),
    THE_INDIAN_EXPRESS(NewsPaperDetails.THE_INDIAN_EXPRESS_ID),
    DOINIK_ITTEFAQ(NewsPaperDetails.DOINIK_ITTEFAQ_ID),
    THE_TIMES_OF_INDIA(NewsPaperDetails.THE_TIMES_OF_INDIA_ID),
    DAILY_MIRROR(NewsPaperDetails.DAILY_MIRROR_ID),
    DHAKA_TRIBUNE(NewsPaperDetails.DHAKA_TRIBUNE_ID),
    BANGLADESH_PROTIDIN(NewsPaperDetails.BANGLADESH_PROTIDIN_ID),
    DAWN(NewsPaperDetails.DAWN_ID),
    KALER_KONTHO(NewsPaperDetails.KALER_KONTHO_ID),
    JUGANTOR(NewsPaperDetails.JUGANTOR_ID),
    THE_FINANCIAL_EXPRESS(NewsPaperDetails.THE_FINANCIAL_EXPRESS_ID),
    BONIK_BARTA(NewsPaperDetails.BONIK_BARTA_ID),
    BHORER_KAGOJ(NewsPaperDetails.BHORER_KAGOJ_ID),
    NEW_AGE(NewsPaperDetails.NEW_AGE_ID),
    DAILY_SUN(NewsPaperDetails.DAILY_SUN_ID),
    JAI_JAI_DIN(NewsPaperDetails.JAI_JAI_DIN_ID),
    AMADER_SOMOY(NewsPaperDetails.AMADER_SOMOY_ID),
    DAILY_OBSERVER(NewsPaperDetails.DAILY_OBSERVER_ID),
    BANGLA_NEWS_24_BN(NewsPaperDetails.BANGLA_NEWS_24_BN_ID),
    BANGLA_NEWS_24_EN(NewsPaperDetails.BANGLA_NEWS_24_EN_ID),
    MANOB_ZAMIN(NewsPaperDetails.MANOB_ZAMIN_ID)
}

