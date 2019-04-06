package com.dasbikash.news_server_parser.parser


import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object JsoupConnector {

    private val TAG = "URLCon"
    val CONNECTION_TIMEOUT_MILLIS = 60000


    fun getDocument(pageUrl: String): Document? {

        var newDocument: Document? = null

        try {
            newDocument = Jsoup.connect(pageUrl).timeout(CONNECTION_TIMEOUT_MILLIS).followRedirects(true).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return newDocument
    }

}