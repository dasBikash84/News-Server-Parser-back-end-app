package com.dasbikash.news_server_parser.utils

object HashUtils {

    fun hash(s: String): String {
        var h = 0L
        for (i in 0 until s.length) {
            h = 31 * h + s[i].toInt()
        }
        return h.toString()
    }
}
