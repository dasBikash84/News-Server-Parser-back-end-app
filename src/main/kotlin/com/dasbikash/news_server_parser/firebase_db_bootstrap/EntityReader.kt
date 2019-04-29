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

package com.dasbikash.news_server_parser.firebase_db_bootstrap

import com.dasbikash.news_server_parser.model.*
import org.hibernate.Session

object EntityReader {

    internal fun getLanguages(session: Session):List<Language>{
        val hql = "FROM ${EntityClassNames.LANGUAGE}"
        val query = session.createQuery(hql, Language::class.java)
        return query.list() as List<Language>
    }

    internal fun getCountries(session: Session):List<Country>{
        val hql = "FROM ${EntityClassNames.COUNTRY}"
        val query = session.createQuery(hql, Country::class.java)
        return query.list() as List<Country>
    }

    internal fun getNewspapers(session: Session):List<Newspaper>{
        val hql = "FROM ${EntityClassNames.NEWSPAPER}"
        val query = session.createQuery(hql, Newspaper::class.java)
        return query.list() as List<Newspaper>
    }

    internal fun getPages(session: Session):List<Page>{
        val hql = "FROM ${EntityClassNames.PAGE}"
        val query = session.createQuery(hql, Page::class.java)
        return query.list() as List<Page>
    }

    internal fun getArticles(session: Session):List<Article>{
        val hql = "FROM ${EntityClassNames.ARTICLE} WHERE articleText is not null"
        val query = session.createQuery(hql, Article::class.java)
        return query.list() as List<Article>
    }

    internal fun getArticlesForPage(session: Session, page: Page):List<Article>{
        val hql = "SELECT * FROM ${DatabaseTableNames.ARTICLE_TABLE_NAME} WHERE pageId='${page.id}' and articleText is not null"
        return session.createNativeQuery(hql, Article::class.java).resultList as List<Article>
    }
}