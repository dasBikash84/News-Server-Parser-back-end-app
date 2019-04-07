package com.dasbikash.news_server_parser.parser.article_body_parsers.dhaka_tribune;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class DhakaTribuneArticleParser extends ArticleBodyParser {

    private static final String TAG = "DTArtLoader";

    private final String mSiteBaseAddress = "https://www.prothomalo.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return DhakaTribuneArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS;
    }

    @Override
    protected String getArticleModificationDateString() {

        Elements dateStringElements =
                mDocument.select(DhakaTribuneArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);

        if (dateStringElements != null && dateStringElements.size()==1) {
            String dateTextFromPage = dateStringElements.get(0).text().trim();
            if (dateTextFromPage.split("at\\s").length == 2) {
                return (dateTextFromPage.split("at\\s"))[1];
            }
        }
        return null;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return DhakaTribuneArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return DhakaTribuneArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return DhakaTribuneArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(DhakaTribuneArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return DhakaTribuneArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return DhakaTribuneArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return DhakaTribuneArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR;
    }
}
