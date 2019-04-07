package com.dasbikash.news_server_parser.parser.article_body_parsers.the_gurdian;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TheGurdianArticleParser extends ArticleBodyParser {

    private static final String TAG = "TGArtLoader";

    private final String mSiteBaseAddress = "https://www.theguardian.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return TheGurdianArticleParserInfo.FEATURED_IMAGE_SELECTOR;
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
        return TheGurdianArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return TheGurdianArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {

        Elements articleDataBlocks = mDocument.select(TheGurdianArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR);

        if (articleDataBlocks != null && articleDataBlocks.size() == 1) {
            Element articleDataBlock = articleDataBlocks.get(0);
            return articleDataBlock.select(TheGurdianArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
        }
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return TheGurdianArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return TheGurdianArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return TheGurdianArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return null;
    }
}
