package com.dasbikash.news_server_parser.parser.preview_page_parsers.prothom_alo;


import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ProthomaloPreviewPageParser extends PreviewPageParser {

    //private static final String TAG = "StackTrace";
    private static final String TAG = "NSUtility";
    public static final String INVALID_DOMAIN_STRING = "paimages";
    public static final String VALID_DOMAIN_REPLACEMENT_STRING = "paloimages";

    private final String mSiteBaseAddress = "https://www.prothomalo.com";

    private static final int MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_WEEKLY_FEATURE = 10;

    /*@Override
    protected int getMaxReRunCountOnEmptyWithRepeat() {
        if (mFeature.isWeekly()) {
            return MAX_RERUN_COUNT_FOR_EMPTY_WITH_REPEAT_FOR_WEEKLY_FEATURE;
        }
        return super.getMaxReRunCountOnEmptyWithRepeat();
    }*/

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String processArticlePreviewImageLink(String previewImageLink) {
        if (previewImageLink.contains(INVALID_DOMAIN_STRING)){
            previewImageLink = previewImageLink.replace(INVALID_DOMAIN_STRING, VALID_DOMAIN_REPLACEMENT_STRING);
        }
        return super.processArticlePreviewImageLink(previewImageLink);
    }

    @Override
    protected String getArticlePublicationDatetimeFormat() {
        return ProthomaloPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TIME_FORMAT;
    }

    @Override
    protected Elements getPreviewBlocks() {

        Elements previewBlockContainers =
                mDocument.select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_CONTAINER_SELECTOR);
        Elements previewBlocks = new Elements();

        if (previewBlockContainers.size() == 0){
            return null;
        } else if (previewBlockContainers.size() == 2){
            previewBlocks = previewBlockContainers.get(0).
                                select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR);
        } else {
            for (Element previewBlockContainer :
                    previewBlockContainers) {
                previewBlocks.addAll(previewBlockContainer.
                                    select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_BLOCK_SELECTOR));
            }
        }
        return previewBlocks;
    }

    @Override
    protected String getArticleLink(Element previewBlock) {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_LINK_ELEMENT_SELECTOR).
                                        get(0).attr(ProthomaloPreviewPageParserInfo.ARTICLE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticlePreviewImageLink(Element previewBlock) {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR).
                                        get(0).attr(ProthomaloPreviewPageParserInfo.ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG);
    }

    @Override
    protected String getArticleTitle(Element previewBlock) {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_TITLE_ELEMENT_SELECTOR).
                                    get(0).text();
    }

    @Override
    protected String getArticlePublicationDateString(Element previewBlock) {
        return previewBlock.select(ProthomaloPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR).
                                        get(0).attr(ProthomaloPreviewPageParserInfo.ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG);
    }

}