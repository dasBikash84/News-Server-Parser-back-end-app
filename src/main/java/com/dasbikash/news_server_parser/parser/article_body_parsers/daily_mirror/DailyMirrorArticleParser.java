package com.dasbikash.news_server_parser.parser.article_body_parsers.daily_mirror;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class DailyMirrorArticleParser extends ArticleBodyParser {

    private static final String TAG = "DMALoader";

    private final String mSiteBaseAddress = "https://www.mirror.co.uk";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getArticleModificationDateString() {

        Elements dateStringElements =
                mDocument.select(DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_BLOCK_SELECTOR);
        //Log.d(TAG, "parseArticle: dateStringElements.size(): "+dateStringElements.size());
        if (dateStringElements != null && dateStringElements.size()>0) {
            String dateString = "";
            if (dateStringElements.size() == 2) {
                dateString = dateStringElements.get(1).
                        attr(DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR_ATTR).trim();
            } else {
                dateString = dateStringElements.get(0).
                        attr(DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR_ATTR).trim();
            }
            return dateString;
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{DailyMirrorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected Elements getArticleFragmentBlocks() {

        Elements articleDataBloacks = mDocument.select(DailyMirrorArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR);
        if (articleDataBloacks.size() !=1) return null;
        return articleDataBloacks.get(0).select(DailyMirrorArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageSelector() {
        return DailyMirrorArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return null;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return DailyMirrorArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return DailyMirrorArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return null;
    }
}
