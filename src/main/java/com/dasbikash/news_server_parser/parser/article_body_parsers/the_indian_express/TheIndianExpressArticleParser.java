package com.dasbikash.news_server_parser.parser.article_body_parsers.the_indian_express;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TheIndianExpressArticleParser extends ArticleBodyParser {

    private final String mSiteBaseAddress = "https://indianexpress.com";
    private Element mArticleDataBlock;

    private final String[] PARA_FILTER_TEXT =
            {
                    "Indian Express App",
                    "Express Editorial",
                    "Express Opinion",
                    "READ |",
                    "Express Explained"
            };

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    {
        for (String filterText:
                PARA_FILTER_TEXT) {
            mParagraphInvalidatorText.add(filterText);
        }
    };

    @Override
    protected String getArticleModificationDateString() {
        Elements articleDataBlocks =
                mDocument.select(TheIndianExpressArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR);

        if (articleDataBlocks.size() == 1) {
            mArticleDataBlock = articleDataBlocks.get(0);
            if (mArticleDataBlock!=null && mArticle.getPublicationTS() == null) {
                Elements dateTimeBlocks = mArticleDataBlock.select(
                        TheIndianExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_SELECTOR
                );
                Element dateTimeBlock = null;
                if (dateTimeBlocks.size() == 1) {
                    dateTimeBlock = dateTimeBlocks.first();
                }
                if (dateTimeBlock != null) {
                    return dateTimeBlock.attr(TheIndianExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_SELECTOR_ATTR);
                }
            }
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{TheIndianExpressArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        if (mArticleDataBlock !=null){
            return mArticleDataBlock.select(TheIndianExpressArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
        }
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return TheIndianExpressArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return null;
    }

    @Override
    protected String getFeaturedImageSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return TheIndianExpressArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return TheIndianExpressArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return null;
    }
}
