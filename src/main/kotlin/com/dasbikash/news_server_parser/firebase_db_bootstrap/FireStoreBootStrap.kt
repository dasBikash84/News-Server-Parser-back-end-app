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
import com.dasbikash.news_server_parser.model.Page
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.database.*
import org.hibernate.Session
import java.io.FileInputStream

object FireStoreBootStrap {
    private lateinit var mRootReference:DatabaseReference
    private lateinit var mFirebaseDatabasePageArticleMapRef: DatabaseReference
    private lateinit var mFirebaseDatabasePageArticleMapUpdateTimeRef: DatabaseReference

    @JvmStatic
    fun main(args: Array<String>) {
        val session = DbSessionManager.getNewSession()

        val serviceAccount = FileInputStream("dontTrack/newsserver-bdb31-firebase-adminsdk-x4lq3-7ab25285b7.json")
        val credentials = GoogleCredentials.fromStream(serviceAccount)

        val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build()

        FirebaseApp.initializeApp(options)
        val db = FirestoreClient.getFirestore()

        mRootReference = FirebaseDatabase.getInstance("https://newsserver-bdb31.firebaseio.com/").reference
        mFirebaseDatabasePageArticleMapRef = mRootReference.child("page_article_map")
        mFirebaseDatabasePageArticleMapUpdateTimeRef = mRootReference.child("page_article_map_update_time")

//        writeRemainingArticles(session, db)

    }

    private fun writeRemainingArticles(session: Session, db: Firestore) {
        val lock = Object()
        val pageUpdateTimeMap: PageArticleMapUpdateTime = PageArticleMapUpdateTime()

        mFirebaseDatabasePageArticleMapUpdateTimeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                throw Throwable(error?.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.children?.asSequence()?.forEach {
                    pageUpdateTimeMap.entries.put(it.key, it.value as Long)
                }
                synchronized(lock) {
                    lock.notify()
                }
            }
        })
        synchronized(lock) {
            lock.wait()
        }
        val pages = EntityReader.getPages(session)
        pages.forEach {
            if (!pageUpdateTimeMap.entries.keys.contains(it.id)) {
                writeArticles(session, db, it)
//                println("${it.newspaper!!.name}: ${it.name}")
            }
        }
    }

    private fun loadAll(session: Session,db:Firestore){
        writeLanguages(session, db)
        writeCountries(session, db)
        writeNewspapers(session, db)
        writePages(session, db)
        writeArticles(session, db)
    }

    private fun writeLanguages(session: Session, db: Firestore) {
        val languages = EntityReader.getLanguages(session)
        val languageCollectionRef = db.collection("languages")
        val batch = db.batch()

        languages.forEach {
            batch.set(languageCollectionRef.document(), LanguageForFS.getFromLanguage(it))
        }
        println()
        println("#################################################################")
        println("Going to write \"Language\" data to Cloud Firestore")
        println("#################################################################")

        val future = batch.commit()
        for (result in future.get()) {
            println("Language entry Update time : ${result.updateTime}")
        }
    }

    private fun writeCountries(session: Session, db: Firestore) {
        val countries = EntityReader.getCountries(session)
        val countryCollectionRef = db.collection("countries")
        val batch = db.batch()

        countries.forEach {
            batch.set(countryCollectionRef.document(), CountryForFS.getFromCountry(it))
        }
        println()
        println("#################################################################")
        println("Going to write \"Country\" data to Cloud Firestore")
        println("#################################################################")

        val future = batch.commit()
        for (result in future.get()) {
            println("Country entry Update time : ${result.updateTime}")
        }
    }

    private fun writeNewspapers(session: Session, db: Firestore) {
        val newspapers = EntityReader.getNewspapers(session)
        val newspaperCollectionRef = db.collection("newspapers")
        val batch = db.batch()

        newspapers.forEach {
            batch.set(newspaperCollectionRef.document(), NewspaperForFS.getFromNewspaper(it))
        }
        println()
        println("#################################################################")
        println("Going to write \"Newspaper\" data to Cloud Firestore")
        println("#################################################################")

        val future = batch.commit()
        for (result in future.get()) {
            println("Newspaper entry Update time : ${result.updateTime}")
        }
    }

    private fun writePages(session: Session, db: Firestore):List<Page> {
        println()
        println("#################################################################")
        println("Going to write \"Page\" data to Cloud Firestore")
        println("#################################################################")

        val pages = EntityReader.getPages(session)
        val pagesCollectionRef = db.collection("pages")
        var batch = db.batch()

        var pageCount = 0
        pages.forEach {
            pageCount++
            batch.set(pagesCollectionRef.document(), PageForFS.getFromPage(it))
            if (pageCount == 400){
                val future = batch.commit()
                for (result in future.get()) {
                    println("Page entry Update time : ${result.updateTime}")
                }
                pageCount = 0
                batch = db.batch()
            }
        }
        if (pageCount > 0){
            val future = batch.commit()
            for (result in future.get()) {
                println("Page entry Update time : ${result.updateTime}")
            }
        }
        return pages
    }

    private fun writeArticles(session: Session, db: Firestore,page: Page) {
        println()
        println("#########################################################################################################")
        println("Going to write \"Article\" data to Cloud Firestore for page: ${page.name} of NP: ${page.newspaper!!.name}")
        println("#########################################################################################################")

        val articles = EntityReader.getArticlesForPage(session,page)
        val articlesCollectionRef = db.collection("articles")
        var batch = db.batch()

        val pageArticleMap = PageArticleMap()

        var articleCount = 0
        articles.forEach {
            articleCount++
            val articleForFs = ArticleForFs.fromArticle(it)
            pageArticleMap.entries.put(articleForFs.id,articleForFs.publicationTime!!.time)
            batch.set(articlesCollectionRef.document(), articleForFs)
            if (articleCount == 400){
                val future = batch.commit()
                for (result in future.get()) {
                    println("Article entry Update time : ${result.updateTime}")
                }
//                return
                articleCount = 0
                batch = db.batch()
            }
        }
        if (articleCount > 0){
            val future = batch.commit()
            for (result in future.get()) {
                println("Article entry Update time : ${result.updateTime}")
            }
        }
        if (pageArticleMap.entries.size >0 ){
            val articleMapUploadTask = mFirebaseDatabasePageArticleMapRef.child(page.id).setValueAsync(pageArticleMap.entries)
            val articleMapUpdateTimeUploadTask = mFirebaseDatabasePageArticleMapUpdateTimeRef.child(page.id).setValueAsync(ServerValue.TIMESTAMP)
            while (!articleMapUploadTask.isDone){}
            while (!articleMapUpdateTimeUploadTask.isDone){}
        }
    }

    private fun writeArticles(session: Session, db: Firestore) {
        println()
        println("#################################################################")
        println("Going to write \"Article\" data to Cloud Firestore")
        println("#################################################################")

        val articles = EntityReader.getArticles(session)
        val articlesCollectionRef = db.collection("articles")
        var batch = db.batch()

        var articleCount = 0
        articles.forEach {
            articleCount++
            batch.set(articlesCollectionRef.document(), ArticleForFs.fromArticle(it))
            if (articleCount == 400){
                val future = batch.commit()
                for (result in future.get()) {
                    println("Article entry Update time : ${result.updateTime}")
                }
//                return
                articleCount = 0
                batch = db.batch()
            }
        }
        if (articleCount > 0){
            val future = batch.commit()
            for (result in future.get()) {
                println("Article entry Update time : ${result.updateTime}")
            }
        }
    }
}

class PageArticleMap{
    val entries = mutableMapOf<String,Long>()
}

class PageArticleMapUpdateTime{
    val entries = mutableMapOf<String,Long>()
}