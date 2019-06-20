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

import com.dasbikash.news_server_parser.model.PageDownloadRequestEntry
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference


object RealTimeDbDataUtils {

    private const val MAX_WAITING_TIME = 60*1000L

    fun addPageDownloadRequest(pageDownloadRequestEntry: PageDownloadRequestEntry): String? {
        val pageDownloadRequest = pageDownloadRequestEntry.getPageDownLoadRequest()

        val lock = Object()
        var documentId:String?=null

        RealTimeDbRefUtils.getPageDownloadRequestRef().push()
                .setValue(pageDownloadRequest,object : DatabaseReference.CompletionListener{
                    override fun onComplete(error: DatabaseError?, ref: DatabaseReference?) {
                        if (error==null && ref!=null){
                            documentId = ref.key
                        }
                        synchronized(lock){lock.notify()}
                    }
                })

        synchronized(lock) { lock.wait(MAX_WAITING_TIME) }

        return documentId
    }

    fun deleteRequest(requestId:String){
        RealTimeDbRefUtils.getPageDownloadRequestRef().child(requestId).setValueAsync(null)
    }
}