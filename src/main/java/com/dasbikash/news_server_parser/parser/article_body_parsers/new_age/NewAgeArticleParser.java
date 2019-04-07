package com.dasbikash.news_server_parser.parser.article_body_parsers.new_age;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class NewAgeArticleParser extends ArticleBodyParser {

    private static final String TAG = "TGArtLoader";

    private final String mSiteBaseAddress = "http://www.newagebd.net";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
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
    protected String getFeaturedImageSelector() {
        return NewAgeArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return NewAgeArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return NewAgeArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {

        Elements articleDataBlocks = mDocument.select(NewAgeArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
        return articleDataBlocks;
    }

    @Override
    protected String getParagraphImageSelector() {
        return NewAgeArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return NewAgeArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return NewAgeArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR;
    }
}
