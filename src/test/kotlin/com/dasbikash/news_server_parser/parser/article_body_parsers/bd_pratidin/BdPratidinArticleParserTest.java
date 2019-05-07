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

package com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BdPratidinArticleParserTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test

    public void splitTest(){
        /*String dateTextFromPage = "প্রকাশ : ১৭ ফেব্রুয়ারি, ২০১৯ ১৭:৫৬ আপডেট : ৭ এপ্রিল, ২০১৯ ১৫:৪২";
        System.out.println("dateTextFromPage"+dateTextFromPage);
        String dateTextFromPageBangla = DisplayUtils.banglaToEnglishDateString(dateTextFromPage);
        System.out.println("dateTextFromPageBangla"+dateTextFromPageBangla);
        System.out.println(dateTextFromPageBangla.replaceAll(".+?:\\s",""));*//*

        String dateString = "২৮ জানুয়ারি, ২০১৯ ১৬:১৩ | পড়া যাবে ১ মিনিটে";
        //System.out.println(dateString);
        dateString = dateString.replaceFirst("\\s\\|.+","").trim();
        //System.out.println(dateString);
        dateString = DisplayUtils.banglaToEnglishDateString(dateString);
        //System.out.println(dateString);
        String linkText = "./asdasdasd";//.substring(1);
        if(linkText.matches("^\\..+")){
            linkText = linkText.substring(1);
        }*/
        //System.out.println(linkText);
    }
}