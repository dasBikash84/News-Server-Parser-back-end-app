package com.dasbikash.news_server_parser.parser.article_body_parsers

import com.dasbikash.news_server_parser.model.Article
import com.dasbikash.news_server_parser.parser.NEWS_PAPER_ID
import com.dasbikash.news_server_parser.utils.ToDoUtils

object ArticleBodyParserFactory {

    fun getArticleBodyParserForArticle(article: Article): ArticleBodyParser? {

        when(article.page?.newspaper?.id){
            NEWS_PAPER_ID.THE_GUARDIAN.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.PROTHOM_ALO.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.ANANDO_BAZAR.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.THE_DAILY_STAR.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.THE_INDIAN_EXPRESS.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.DOINIK_ITTEFAQ.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.THE_TIMES_OF_INDIA.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.DAILY_MIRROR.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.DHAKA_TRIBUNE.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.BANGLADESH_PROTIDIN.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.DAWN.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.KALER_KONTHO.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.JUGANTOR.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.THE_FINANCIAL_EXPRESS.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.BONIK_BARTA.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.BHORER_KAGOJ.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.NEW_AGE.id -> ToDoUtils.workToDo()
            NEWS_PAPER_ID.DAILY_SUN.id -> ToDoUtils.workToDo()
            else-> null
        }
        return null
    }
}
