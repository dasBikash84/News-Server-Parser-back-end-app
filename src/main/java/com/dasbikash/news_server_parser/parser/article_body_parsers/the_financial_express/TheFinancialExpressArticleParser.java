package com.dasbikash.news_server_parser.parser.article_body_parsers.the_financial_express;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class TheFinancialExpressArticleParser extends ArticleBodyParser {

    private final String mSiteBaseAddress = "http://www.kalerkantho.com";

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
                mDocument.select(TheFinancialExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);

        if (dateStringElements != null && dateStringElements.size()>0) {
            String dateString = dateStringElements.get(0).text().trim();
            //Log.d(TAG, "parseArticle: dateString:"+dateString);
            dateString = dateString.replaceFirst("^.+?:","").trim();
            //Log.d(TAG, "parseArticle: dateString:"+dateString);
            return dateString;
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{TheFinancialExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected String getFeaturedImageSelector() {
        return TheFinancialExpressArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return TheFinancialExpressArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return TheFinancialExpressArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(TheFinancialExpressArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return TheFinancialExpressArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return TheFinancialExpressArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return TheFinancialExpressArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR;
    }
}
