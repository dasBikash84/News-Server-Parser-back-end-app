package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_financial_express;


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TheFinancialExpressPreviewPageParser extends PreviewPageParser {

    //private static final String TAG = "StackTrace";
    private static final String TAG = "TFEEdLoader";

    private final String mSiteBaseAddress = "http://thefinancialexpress.com.bd";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return null;
    }

    int mPreviewBlockType=0;

    @Override
    protected Elements getPreviewBlocks() {
        return mDocument.select(TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
    }

    @Override
    protected String getArticleLink(Element previewBlock) {
        if (previewBlock.is(TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR2)){
            mPreviewBlockType = 1;
        }
        String articleLink;
        if (mPreviewBlockType == 0) {
            articleLink = previewBlock.select(TheFinancialExpressPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR[mPreviewBlockType]).
                                                get(0).attr(TheFinancialExpressPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mPreviewBlockType]);
        }else {
            articleLink = previewBlock.attr(TheFinancialExpressPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mPreviewBlockType]);
        }

        if (articleLink ==null || articleLink.trim().length()==0){
            return null;
        }
        return articleLink;
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(
                TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).get(0).
                attr(TheFinancialExpressPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG
        );
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        return previewBlock.select(TheFinancialExpressPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mPreviewBlockType]).
                                    get(0).text();
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        return null;
    }

    @Override
    protected String processArticleLink(String articleLink) {
        if (!articleLink.contains(mSiteBaseAddress) &&
                !articleLink.matches("^//.+")&&
                !articleLink.matches("^/.+")){
            articleLink = "/"+articleLink;
        }
        return super.processArticleLink(articleLink);
    }

    @Override
    protected String processArticlePreviewImageLink(String previewImageLink) {
        if (!previewImageLink.contains(mSiteBaseAddress) &&
                !previewImageLink.matches("^//.+")&&
                !previewImageLink.matches("^/.+")){
            previewImageLink = "/"+previewImageLink;
        }
        return super.processArticlePreviewImageLink(previewImageLink);
    }
}