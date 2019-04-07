package com.dasbikash.news_server_parser.parser.article_body_parsers.the_times_of_india;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class TheTimesOfIndiaArticleParser extends ArticleBodyParser {

    //private static final String TAG = "StackTrace";
    private static final String TAG = "TTOIArticleLoader";

    private final String mSiteBaseAddress = "https://timesofindia.indiatimes.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return TheTimesOfIndiaArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getArticleModificationDateString() {
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[0];
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return TheTimesOfIndiaArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return TheTimesOfIndiaArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return null;
    }

    private int mParagraphLayoutType = 0;

    @Override
    protected Elements getArticleFragmentBlocks() {

        Elements articleFragmentBlocks =
                mDocument.select(TheTimesOfIndiaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mParagraphLayoutType]);

        if (articleFragmentBlocks.size()>0){
            return articleFragmentBlocks;
        }
        mParagraphLayoutType++;

        articleFragmentBlocks =
                mDocument.select(TheTimesOfIndiaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mParagraphLayoutType]);

        if (articleFragmentBlocks.size()>0){
            return articleFragmentBlocks;
        }
        mParagraphLayoutType++;

        articleFragmentBlocks =
                mDocument.select(TheTimesOfIndiaArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR[mParagraphLayoutType]);

        if (articleFragmentBlocks.size()>0){
            return articleFragmentBlocks;
        }
        return null;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return TheTimesOfIndiaArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR[mParagraphLayoutType];
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return TheTimesOfIndiaArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR[mParagraphLayoutType];
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return TheTimesOfIndiaArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR[mParagraphLayoutType];
    }
}
