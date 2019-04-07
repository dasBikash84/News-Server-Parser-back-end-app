package com.dasbikash.news_server_parser.parser.article_body_parsers.doinick_ittefaq;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.select.Elements;

public class DoinickIttefaqArticleParser extends ArticleBodyParser {

    private final String mSiteBaseAddress = "http://www.ittefaq.com.bd";

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
        return null;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return DoinickIttefaqArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return DoinickIttefaqArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return DoinickIttefaqArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(
                DoinickIttefaqArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR
        );
    }

    @Override
    protected String getParagraphImageSelector() {
        return DoinickIttefaqArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return DoinickIttefaqArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return DoinickIttefaqArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR;
    }
}
