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

package com.dasbikash.news_server_parser.parser.preview_page_parsers.jugantor

import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.DisplayUtils
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar


class JugantorPreviewPageParser : PreviewPageParser() {

    private val mSiteBaseAddress = "https://www.jugantor.com"

    override fun getSiteBaseAddress(): String {
        return mSiteBaseAddress
    }

    override fun getArticlePublicationDatetimeFormat(): String {
        return JugantorPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT
    }

    override fun getPreviewBlocks(): Elements {
        return mDocument.select(JugantorPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR)
    }

    override fun getArticleLink(previewBlock: Element): String {
        return previewBlock.select(JugantorPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR)[0].attr(JugantorPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticlePreviewImageLink(previewBlock: Element): String {
        return previewBlock.select(JugantorPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR)[0].attr(JugantorPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG)
    }

    override fun getArticleTitle(previewBlock: Element): String {
        return previewBlock.select(JugantorPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR)[0].text()
    }

    override fun getArticlePublicationDateString(previewBlock: Element): String? {
        return null
    }

    override fun processArticlePreviewImageLink(previewImageLink: String): String? {
        var previewImageLink = previewImageLink
        if (!previewImageLink.matches(".+?url\\(.+?\\).+".toRegex())) {
            return null
        }
        previewImageLink = previewImageLink.split("url\\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        previewImageLink = previewImageLink.split("\\)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        //return previewImageLink;
        return super.processArticlePreviewImageLink(previewImageLink)
    }

    override fun getArticlePublicationTimeStamp(previewBlock: Element): Long? {
        val articlePublicationDateString = previewBlock.select(JugantorPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR)[0].text()
        return processDateString(articlePublicationDateString, mSimpleDateFormat)
    }

    private fun processDateString(articlePublicationDateString: String, simpleDateFormat: SimpleDateFormat): Long {
        var articlePublicationDateString = articlePublicationDateString
        //Log.d(TAG, "processDateString: articlePublicationDateString: "+articlePublicationDateString);
        if (articlePublicationDateString.contains(HOURS_AGO_TIME_STRING_BANGLA)) {
            articlePublicationDateString = articlePublicationDateString.replace(HOURS_AGO_TIME_STRING_BANGLA, "").trim { it <= ' ' }
            var agoHour = 0
            try {
                agoHour = Integer.decode(articlePublicationDateString)!!
                //Log.d(TAG, "processDateString: agoHour: " + agoHour);
                val publicationTime = Calendar.getInstance()
                publicationTime.timeInMillis = publicationTime.timeInMillis - agoHour * ONE_HOUR_IN_MILLIS
                return publicationTime.timeInMillis
            } catch (ex: Exception) {
                //Log.d(TAG, "processDateString: Error: "+ex.getMessage());
                return 0L
            }

        } else if (articlePublicationDateString.contains(MINUTES_AGO_TIME_STRING_BANGLA)) {
            articlePublicationDateString = articlePublicationDateString.replace(MINUTES_AGO_TIME_STRING_BANGLA, "").trim { it <= ' ' }
            var agoMinutes = 0
            try {
                agoMinutes = Integer.decode(articlePublicationDateString)!!
                //Log.d(TAG, "processDateString: agoMinutes: " + agoMinutes);
                val publicationTime = Calendar.getInstance()
                publicationTime.timeInMillis = publicationTime.timeInMillis - agoMinutes * ONE_MINUTE_IN_MILLIS
                return publicationTime.timeInMillis
            } catch (ex: Exception) {
                //Log.d(TAG, "processDateString: Error: "+ex.getMessage());
                return 0L
            }

        } else {
            //Log.d(TAG, "processDateString: GeneralTimeString: "+articlePublicationDateString);
            articlePublicationDateString = DisplayUtils.banglaToEnglishDateString(articlePublicationDateString).trim { it <= ' ' }
            //Log.d(TAG, "processDateString: inEnglish: "+articlePublicationDateString);
            val publicationTime = Calendar.getInstance()
            publicationTime.timeZone = simpleDateFormat.timeZone
            try {
                publicationTime.time = simpleDateFormat.parse(articlePublicationDateString)
                return publicationTime.timeInMillis
            } catch (e: ParseException) {
//                e.printStackTrace()
                return 0L
            }

        }
    }

    companion object {

        //private static final String TAG = "StackTrace";
        private val TAG = "JugEdLoader"

        private val MINUTES_AGO_TIME_STRING_BANGLA = "মি. আগে"
        private val HOURS_AGO_TIME_STRING_BANGLA = "ঘ. আগে"
        val ONE_HOUR_IN_MILLIS = 3600000
        val ONE_MINUTE_IN_MILLIS = 60000
    }

}