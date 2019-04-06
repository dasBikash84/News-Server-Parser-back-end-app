package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_gurdian;


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TheGurdianPreviewPageParser extends PreviewPageParser {

    private static final String TAG = "TheGurdianPreviewPageParser";

    private final String mSiteBaseAddress = "https://www.theguardian.com";

    private final String TITLE_FILTER_REGEX = "(?i).+?(" +
                                            "(–\\s?podcast\\s?)|" +
                                            "(-\\s?podcast\\s?)|" +
                                            "(–\\s?video\\s?)|" +
                                            "(-\\s?video\\s?)|" +
                                            "(–.+?live updates\\s?)|" +
                                            "(-.+?live updates\\s?)|" +
                                            "(–.+?live\\s?)|" +
                                            "(-.+?live\\s?)|" +
                                            "(–\\s?cartoon\\s?)|" +
                                            "(-\\s?cartoon\\s?)|" +
                                            "(–\\s?in pictures\\s?)|" +
                                            "(-\\s?in pictures\\s?)|" +
                                            "(\\s?best photos\\s?)|" +
                                            "(–\\s?as it happened\\s?)|" +
                                            "(-\\s?as it happened\\s?)" +
                                            ")\\s?$";

    private static final int MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE = 3;

    /*@Override
    protected int getMaxReRunCountOnEmptyWithRepeat() {
        return MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_REGULAR_FEATURE;
    }*/

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return TheGurdianPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT;
    }

    @Override
    protected Long getArticlePublicationTimeStamp(Element previewBlock) {
        return null;
    }

    @Override
    protected Elements getPreviewBlocks() {
        return mDocument.select(TheGurdianPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
    }

    @Override
    protected String getArticleLink(Element previewBlock) {
        return previewBlock.select(TheGurdianPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR).get(0).
                                    attr(TheGurdianPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(TheGurdianPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).get(0).
                                    attr(TheGurdianPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        String articleTitle = previewBlock.select(TheGurdianPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR).
                                    get(0).text();
        if (articleTitle.matches(TITLE_FILTER_REGEX)){
            return null;
        }
        return articleTitle;
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        return previewBlock.select(
                TheGurdianPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR).
                get(0).attr(TheGurdianPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG
        );
    }
}