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

package com.dasbikash.news_server_parser.parser.article_body_parsers

import com.dasbikash.news_server_parser.model.Article
import com.dasbikash.news_server_parser.parser.NEWS_PAPER_ID
import com.dasbikash.news_server_parser.parser.article_body_parsers.anando_bazar.AnandoBazarArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin.BdPratidinArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.bhorer_kagoj.BhorerKagojArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.bonik_barta.BonikBartaArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.daily_mirror.DailyMirrorArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.daily_observer.DailyObserverArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.daily_sun.DailySunArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.dainik_amader_shomoy.AmaderSomoyArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.dawn.DawnArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.dhaka_tribune.DhakaTribuneArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.doinick_ittefaq.DoinickIttefaqArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.jaijaidin.JaiJaiDinArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.jugantor.JugantorArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.kaler_kantho.KalerKanthoArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.new_age.NewAgeArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.prothom_alo.ProthomaloArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.the_daily_star.TheDailyStarArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.the_financial_express.TheFinancialExpressArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.the_gurdian.TheGurdianArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.the_indian_express.TheIndianExpressArticleParser
import com.dasbikash.news_server_parser.parser.article_body_parsers.the_times_of_india.TheTimesOfIndiaArticleParser

internal object ArticleBodyParserFactory {

    fun getArticleBodyParserForArticle(article: Article): ArticleBodyParser? {

        return when(article.page?.newspaper?.id){
            NEWS_PAPER_ID.THE_GUARDIAN.id -> TheGurdianArticleParser()
            NEWS_PAPER_ID.PROTHOM_ALO.id -> ProthomaloArticleParser()
            NEWS_PAPER_ID.ANANDO_BAZAR.id -> AnandoBazarArticleParser()
            NEWS_PAPER_ID.THE_DAILY_STAR.id -> TheDailyStarArticleParser()
            NEWS_PAPER_ID.DOINIK_ITTEFAQ.id -> DoinickIttefaqArticleParser()
            NEWS_PAPER_ID.THE_TIMES_OF_INDIA.id -> TheTimesOfIndiaArticleParser()
            NEWS_PAPER_ID.DAILY_MIRROR.id -> DailyMirrorArticleParser()
            NEWS_PAPER_ID.DHAKA_TRIBUNE.id -> DhakaTribuneArticleParser()
            NEWS_PAPER_ID.DAWN.id -> DawnArticleParser()
            NEWS_PAPER_ID.KALER_KONTHO.id -> KalerKanthoArticleParser()
            NEWS_PAPER_ID.JUGANTOR.id -> JugantorArticleParser()
            NEWS_PAPER_ID.THE_FINANCIAL_EXPRESS.id -> TheFinancialExpressArticleParser()
            NEWS_PAPER_ID.BONIK_BARTA.id -> BonikBartaArticleParser()
            NEWS_PAPER_ID.BHORER_KAGOJ.id -> BhorerKagojArticleParser()
            NEWS_PAPER_ID.NEW_AGE.id -> NewAgeArticleParser()
            NEWS_PAPER_ID.DAILY_SUN.id -> DailySunArticleParser()
            NEWS_PAPER_ID.THE_INDIAN_EXPRESS.id -> TheIndianExpressArticleParser()
            NEWS_PAPER_ID.BANGLADESH_PROTIDIN.id -> BdPratidinArticleParser()
            NEWS_PAPER_ID.JAI_JAI_DIN.id -> JaiJaiDinArticleParser()
            NEWS_PAPER_ID.AMADER_SOMOY.id -> AmaderSomoyArticleParser()
            NEWS_PAPER_ID.DAILY_OBSERVER.id -> DailyObserverArticleParser()
            else-> null
        }
    }
}
