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
        return siteBaseAddress+linkText;
    }
}
