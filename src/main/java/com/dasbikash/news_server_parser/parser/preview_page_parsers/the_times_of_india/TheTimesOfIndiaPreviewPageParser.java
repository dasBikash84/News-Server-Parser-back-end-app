package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_times_of_india;


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TheTimesOfIndiaPreviewPageParser extends PreviewPageParser {

    private static final String TAG = "ProthomaloEditionLoader";

    private static final String REGEX_FOR_SPORTS_LINK = ".+?/sports/.+?/page.+";
    private static final String REGEX_FOR_ENT_LIFESTYLE_LINK = ".+?/(entertainment|life\\-style)/.+?";
    private static final String REGEX_FOR_LINK_WITH_HTTPS = "^https:.+";

    private static int GENERAL_ARTICLE_PARSER_INDEX = 0;
    private static int ENT_LIFESTYLE_ARTICLE_PARSER_INDEX = 1;
    private static int SPORTS_ARTICLE_PARSER_INDEX = 2;
    private int mArticleParserIndex = GENERAL_ARTICLE_PARSER_INDEX;

    private final String mSiteBaseAddress = "https://timesofindia.indiatimes.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getPageLink() {

        if (mCurrentPage.getLinkFormat().matches(REGEX_FOR_ENT_LIFESTYLE_LINK)){
            mArticleParserIndex = ENT_LIFESTYLE_ARTICLE_PARSER_INDEX;
        } else if (mCurrentPage.getLinkFormat().matches(REGEX_FOR_SPORTS_LINK)){
            mArticleParserIndex = SPORTS_ARTICLE_PARSER_INDEX;
        }

        if ((mArticleParserIndex == GENERAL_ARTICLE_PARSER_INDEX || mArticleParserIndex == SPORTS_ARTICLE_PARSER_INDEX)
            && mCurrentPageNumber == 1){
            if (mCurrentPage.getLinkVariablePartFormat() !=null) {
                return mCurrentPage.getLinkFormat().replace(mCurrentPage.getLinkVariablePartFormat(), "");
            }
        }
        return super.getPageLink();
    }

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT[mArticleParserIndex];
    }

    @Override
    protected Elements getPreviewBlocks() {
        return mDocument.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR[mArticleParserIndex]);
    }

    @Override
    protected String getArticleLink(Element previewBlock) {
        return previewBlock.select(
                TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mArticleParserIndex]).
                get(0).select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR[mArticleParserIndex]).
                get(0).attr(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG[mArticleParserIndex]
        );
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(
                TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR[mArticleParserIndex]).
                get(0).attr(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG[mArticleParserIndex]
        );
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        return previewBlock.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR[mArticleParserIndex]).
                                    get(0).text();
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        if (mArticleParserIndex == ENT_LIFESTYLE_ARTICLE_PARSER_INDEX) {
            return previewBlock.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[mArticleParserIndex]).
                    get(0).attr(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG[mArticleParserIndex]);
        }else {
            return previewBlock.select(TheTimesOfIndiaPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[mArticleParserIndex]).
                    get(0).text();
        }
    }
}