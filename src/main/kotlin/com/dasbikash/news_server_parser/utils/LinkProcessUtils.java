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

package com.dasbikash.news_server_parser.utils;

public class LinkProcessUtils {

    private static final String sSeperateDomainIdentifierRegex = "^//.+";
    private static final String sInvalidHTTPProtocol = "http:";

    private static String getSiteHTTPProtocol(String siteBaseAddress){

        if (siteBaseAddress.matches("^https:.+")){
            return "https:";
        } else  if(siteBaseAddress.matches("^http:.+")){
            return "http:";
        } else {
            return null;
        }
    }

    public static String processLink(String linkText,String siteBaseAddress){

        String siteHTTPString = getSiteHTTPProtocol(siteBaseAddress);

        if (siteHTTPString == null) return null;

        if (linkText.contains(siteHTTPString)) return linkText;

        if (linkText.contains(sInvalidHTTPProtocol)){
            return linkText.replace(sInvalidHTTPProtocol,siteHTTPString);
        }

        if (linkText.matches(sSeperateDomainIdentifierRegex)){
            return siteHTTPString+linkText;
        }
        if(!linkText.matches("^/.+")){
            linkText = "/"+linkText;
        }
        if(linkText.matches("^\\..+")){
            linkText = linkText.substring(1);
        }
        return siteBaseAddress+linkText;
    }
}