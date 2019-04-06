package com.dasbikash.news_server_parser.parser.article_body_parsers;

import com.dasbikash.news_server_parser.model.Article;

abstract public class ArticleBodyParser {
    public abstract Article loadArticleBody(Article article);
}