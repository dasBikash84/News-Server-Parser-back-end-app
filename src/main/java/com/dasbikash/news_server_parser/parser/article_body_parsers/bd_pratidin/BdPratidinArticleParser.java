package com.dasbikash.news_server_parser.parser.article_body_parsers.bd_pratidin;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import com.dasbikash.news_server_parser.utils.DisplayUtils;
import org.jsoup.select.Elements;

public class BdPratidinArticleParser extends ArticleBodyParser {

    private static final String TAG = "BdPArtLoader";
    public static final String DATE_STRING_SPLITTER_REGEX = "প্রকাশ\\s:\\s";

    private final String mSiteBaseAddress = "http://www.bd-pratidin.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String processLink(String linkText) {
        if (linkText.matches("^\\./.+")){
            linkText = linkText.substring(1);
        }
        return super.processLink(linkText);
    }

    @Override
    protected String getArticleModificationDateString() {

        Elements dateStringElements =
                mDocument.select(BdPratidinArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);

        if (dateStringElements != null && dateStringElements.size()>=1) {
            //Log.d(TAG, "parseArticle: dateText: "+document.select(".detail-poauthor li").get(0).text().trim());
            String dateTextFromPage = dateStringElements.get(0).text().trim();

            if (dateTextFromPage.split(DATE_STRING_SPLITTER_REGEX).length == 2) {
                String dateString = ((dateTextFromPage.split(DATE_STRING_SPLITTER_REGEX))[1]).trim();
                //Log.d(TAG, "parseArticle: dateString:"+dateString);
                dateString = DisplayUtils.banglaToEnglishDateString(dateString);
                return dateString;
            }
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{BdPratidinArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return BdPratidinArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(BdPratidinArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return BdPratidinArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR;
    }
}
