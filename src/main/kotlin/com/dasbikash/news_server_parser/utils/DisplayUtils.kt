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

package com.dasbikash.news_server_parser.utils

object DisplayUtils {

    private val TAG = "Stack1Trace1"

    private val JUST_NOW_TIME_STRING = "Just now"
    private val JUST_NOW_TIME_STRING_BANGLA = "এইমাত্র পাওয়া"
    private val MINUTES_TIME_STRING = "minutes"
    private val MINUTE_TIME_STRING = "minute"
    private val MINUTE_TIME_STRING_BANGLA = "মিনিট"
    private val AGO_TIME_STRING = "ago"
    private val AGO_TIME_STRING_BANGLA = "আগে"
    private val HOURS_TIME_STRING = "hours"
    private val HOUR_TIME_STRING = "hour"
    private val HOUR_TIME_STRING_BANGLA = "ঘণ্টা"
    private val YESTERDAY_TIME_STRING = "Yesterday"
    private val YESTERDAY_TIME_STRING_BANGLA = "গতকাল"

    private val BANGLA_UNICODE_ZERO: Char = 0x09E6.toChar()
    private val BANGLA_UNICODE_NINE: Char = 0x09EF.toChar()
    private val ENGLISH_UNICODE_ZERO: Char = 0x0030.toChar()
    private val ENGLISH_UNICODE_NINE: Char = 0x0039.toChar()

    private val MONTH_NAME_TABLE = arrayOf(arrayOf("জানুয়ারী", "Jan"), arrayOf("জানুয়ারি", "Jan"), arrayOf("জানুয়ারি", "Jan"), arrayOf("ফেব্রুয়ারী", "Feb"), arrayOf("ফেব্রুয়ারি", "Feb"), arrayOf("ফেব্রুয়ারি", "Feb"), arrayOf("মার্চ", "Mar"), arrayOf("এপ্রিল", "Apr"), arrayOf("এপ্রিল", "Apr"), arrayOf("মে", "May"), arrayOf("জুন", "Jun"), arrayOf("জুলাই", "Jul"), arrayOf("আগস্ট", "Aug"), arrayOf("আগষ্ট", "Aug"), arrayOf("অগস্ট", "Aug"), arrayOf("সেপ্টেম্বর", "Sep"), arrayOf("অক্টোবর", "Oct"), arrayOf("অক্টোবর", "Oct"), arrayOf("নভেম্বর", "Nov"), arrayOf("ডিসেম্বর", "Dec"))
    private val DAY_NAME_TABLE = arrayOf(arrayOf("শনিবার", "Sat"), arrayOf("রবিবার", "Sun"), arrayOf("সোমবার", "Mon"), arrayOf("মঙ্গলবার", "Tue"), arrayOf("বুধবার", "Wed"), arrayOf("বৃহস্পতিবার", "Thu"), arrayOf("শুক্রবার", "Fri"))

    private val AM_PM_MARKER_TABLE = arrayOf(arrayOf("পূর্বাহ্ণ", "AM"), arrayOf("অপরাহ্ণ", "PM"),
                                                                arrayOf("এএম", "AM"), arrayOf("পিএম", "PM"),
                                                                arrayOf("পূর্বাহ্ণ", "am"), arrayOf("অপরাহ্ণ", "pm"))

    private fun replaceBanglaMonthName(str: String): String {
        for (i in MONTH_NAME_TABLE.indices) {
            if (str.contains(MONTH_NAME_TABLE[i][0])) {
                return str.replace(MONTH_NAME_TABLE[i][0], MONTH_NAME_TABLE[i][1])
            }
        }
        return str
    }

    private fun replaceAMPMMarkerBanToEng(str: String): String {
        for (i in AM_PM_MARKER_TABLE.indices) {
            if (str.contains(AM_PM_MARKER_TABLE[i][0])) {
                return str.replace(AM_PM_MARKER_TABLE[i][0], AM_PM_MARKER_TABLE[i][1])
            }
        }
        return str
    }

    private fun replaceAMPMMarkerEngToBan(str: String): String {
        for (i in AM_PM_MARKER_TABLE.indices) {
            if (str.contains(AM_PM_MARKER_TABLE[i][1])) {
                return str.replace(AM_PM_MARKER_TABLE[i][1], AM_PM_MARKER_TABLE[i][0])
            }
        }
        return str
    }

    private fun replaceEnglishMonthName(str: String): String {
        for (i in MONTH_NAME_TABLE.indices) {
            if (str.contains(MONTH_NAME_TABLE[i][1])) {
                return str.replace(MONTH_NAME_TABLE[i][1], MONTH_NAME_TABLE[i][0])
            }
        }
        return str
    }

    private fun replaceEnglishDayName(str: String): String {
        for (i in DAY_NAME_TABLE.indices) {
            if (str.contains(DAY_NAME_TABLE[i][1])) {
                return str.replace(DAY_NAME_TABLE[i][1], DAY_NAME_TABLE[i][0])
            }
        }
        return str
    }

    private fun replaceBanglaDigits(string: String): String {

        val chars = string.toCharArray()

        for (i in chars.indices) {
            val ch = chars[i]
            if (ch <= BANGLA_UNICODE_NINE && ch >= BANGLA_UNICODE_ZERO) {
                chars[i] = ch + ENGLISH_UNICODE_ZERO.toInt() - BANGLA_UNICODE_ZERO.toInt()
            }
        }

        return String(chars)
    }

    private fun replaceEnglishDigits(string: String): String {

        val chars = string.toCharArray()

        for (i in chars.indices) {
            val ch = chars[i]
            if (ch <= ENGLISH_UNICODE_NINE && ch >= ENGLISH_UNICODE_ZERO) {
                chars[i] = ch + BANGLA_UNICODE_ZERO.toInt() - ENGLISH_UNICODE_ZERO.toInt()
            }
        }

        return String(chars)
    }

    private fun convertToBanglaTimeString(publicationTimeStr: String): String {
        var publicationTimeString = publicationTimeStr

        if (publicationTimeString == JUST_NOW_TIME_STRING) return JUST_NOW_TIME_STRING_BANGLA
        if (publicationTimeString == YESTERDAY_TIME_STRING) return YESTERDAY_TIME_STRING_BANGLA
        if (publicationTimeString.contains(AGO_TIME_STRING)) {
            publicationTimeString = publicationTimeString.replace(AGO_TIME_STRING, AGO_TIME_STRING_BANGLA)
            if (publicationTimeString.contains(MINUTES_TIME_STRING)) {
                publicationTimeString = publicationTimeString.replace(MINUTES_TIME_STRING, MINUTE_TIME_STRING_BANGLA)
            } else {
                publicationTimeString = publicationTimeString.replace(MINUTE_TIME_STRING, MINUTE_TIME_STRING_BANGLA)
            }
            if (publicationTimeString.contains(HOURS_TIME_STRING)) {
                publicationTimeString = publicationTimeString.replace(HOURS_TIME_STRING, HOUR_TIME_STRING_BANGLA)
            } else {
                publicationTimeString = publicationTimeString.replace(HOUR_TIME_STRING, HOUR_TIME_STRING_BANGLA)
            }
            publicationTimeString = replaceEnglishDigits(publicationTimeString)
            return publicationTimeString
        }

        return englishToBanglaDateString(publicationTimeString)
    }

    fun banglaToEnglishDateString(dateStr: String): String {
        var dateString = dateStr
        dateString = replaceBanglaMonthName(dateString)
        dateString = replaceBanglaDigits(dateString)
        dateString = replaceAMPMMarkerBanToEng(dateString)
        return dateString
    }

    fun englishToBanglaDateString(dateStr: String): String {
        var dateString = dateStr
        dateString = replaceEnglishMonthName(dateString)
        dateString = replaceEnglishDigits(dateString)
        dateString = replaceEnglishDayName(dateString)
        dateString = replaceAMPMMarkerEngToBan(dateString)
        return dateString
    }
}
