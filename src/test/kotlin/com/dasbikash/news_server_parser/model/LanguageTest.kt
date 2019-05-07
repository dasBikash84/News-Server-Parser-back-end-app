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

import org.hibernate.Session
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach


internal class LanguageTest {
    lateinit var session: Session
    @BeforeEach
    fun setUp() {
//        session = DbSessionManager.getNewSession()
    }

    @AfterEach
    fun tearDown() {
    }

    /*@Test
    fun readAllLanguage(){
        val hql = "FROM ${EntityClassNames.LANGUAGE}"
        val query = session.createQuery(hql, Language::class.java)
        val languages = query.list() as List<Language>

        languages.forEach {
            println(it)
        }
    }

    @Test
    fun fireStoreLanguageWrite(){
        val hql = "FROM ${EntityClassNames.LANGUAGE}"
        val query = session.createQuery(hql, Language::class.java)
        val languages = query.list() as List<Language>

        // Use a service account
        val serviceAccount = FileInputStream("dontTrack/newsserver-bdb31-firebase-adminsdk-x4lq3-7ab25285b7.json")
        val credentials = GoogleCredentials.fromStream(serviceAccount)

        val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
//                .setDatabaseUrl("https://newsserver-bdb31.firebaseio.com/")
                .build()

        FirebaseApp.initializeApp(options)

        val db = FirestoreClient.getFirestore()

        val languageCollectionRef = db.collection("languages")

//        val langForDbList = mutableListOf<langForDb>()

        val batch = db.batch()

        languages.forEach {
            batch.set(languageCollectionRef.document(),langForDb(it.id,it.name))
//            val result = languageCollectionRef.add(langForDb(it.id,it.name))
//            println("${it.name} saved at id: ${result.get().id}")
        }
        val future = batch.commit()


        for (result in future.get()) {
            //System.out.println("Update time : " + result.updateTime)
        }
        *//*val docRef = db.collection("languages").document("alovelace")
        // Add document data  with id "alovelace" using a hashmap
        val data = HashMap<String, Any>()
        data["first"] = "Ada"
        data["last"] = "Lovelace"
        data["born"] = 1815
        //asynchronously write data
        val result = docRef.set(data)
        // ...
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().updateTime)*//*
    }

    class langForDb(
        var id:String="",
        var name: String?=null
    )*/
}