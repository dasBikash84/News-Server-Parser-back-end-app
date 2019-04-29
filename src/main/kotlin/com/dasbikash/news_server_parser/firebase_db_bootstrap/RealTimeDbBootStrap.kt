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

import com.dasbikash.news_server_parser.database.DbSessionManager
import com.dasbikash.news_server_parser.model.Article
import com.dasbikash.news_server_parser.model.Page
import com.google.api.core.ApiFuture
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import com.google.protobuf.Api
import org.hibernate.Session
import java.io.FileInputStream


object RalTimeDbBootStrap {

    private lateinit var mRootReference:DatabaseReference
    private lateinit var mArticleDataReference:DatabaseReference

    @JvmStatic
    fun main(args: Array<String>) {
        val session = DbSessionManager.getNewSession()

        val serviceAccount = FileInputStream("dontTrack/newsserver-bdb31-firebase-adminsdk-x4lq3-7ab25285b7.json")
        val credentials = GoogleCredentials.fromStream(serviceAccount)

        val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build()

        FirebaseApp.initializeApp(options)

        mRootReference = FirebaseDatabase.getInstance("https://newsserver-bdb31.firebaseio.com/").reference
        mArticleDataReference = mRootReference.child("article_data")

        EntityReader.getPages(session)
                .asSequence()
                .drop(1)
                .forEach {
                    writeArticlesForPage(session, mArticleDataReference.child(it.id),it)
                }
    }

    private fun writeArticlesForPage(session: Session, articleDataRef:DatabaseReference, page: Page) {

        println()
        println("#########################################################################################################")
        println("Going to write \"Article\" data to Real Time DB for page: ${page.name} of NP: ${page.newspaper!!.name}")
        println("#########################################################################################################")

        val articles = EntityReader.getArticlesForPage(session, page)

        val futureList = mutableListOf<ApiFuture<Void>>()
        articles.asSequence().forEach {
            futureList.add(articleDataRef.push().setValueAsync(ArticleForFs.fromArticle(it)))
        }
        futureList.asSequence().forEach {
            while (!it.isDone){}
        }


    }
}
