package com.dasbikash.news_server_parser.parser.article_body_parsers.dawn;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class DawnArticleParser extends ArticleBodyParser {

    private static final String TAG = "StackTrace";

    private final String mSiteBaseAddress = "https://www.dawn.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected int getReqFeaturedImageCount() {
        return DawnArticleParserInfo.REQUIRED_FEATURED_IMAGE_COUNT;
    }

    @Override
    protected int getReqFeaturedImageIndex() {
        return DawnArticleParserInfo.REQUIRED_FEATURED_IMAGE_INDEX;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return DawnArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return DawnArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return DawnArticleParserInfo.FEATURED_IMAGE_SELECTOR;
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
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(DawnArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageSelector() {
        return DawnArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return DawnArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return DawnArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return null;
    }
}
