package com.dasbikash.news_server_parser.parser.article_body_parsers.the_daily_star;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TheDailyStarArticleParser extends ArticleBodyParser {

    private final String mSiteBaseAddress = "https://www.thedailystar.net";
    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String getArticleModificationDateString() {

        Elements dateStringElements =
                mDocument.select(TheDailyStarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);

        if (dateStringElements != null && dateStringElements.size()==1) {

            String dateString = dateStringElements.get(0).text().trim();

            if (dateString.split(TheDailyStarArticleParserInfo.REGEX_TO_EXTRACT_LAST_MODIFIED_STRING).length==2) {
                dateString = dateString.split(TheDailyStarArticleParserInfo.REGEX_TO_EXTRACT_LAST_MODIFIED_STRING)[1];
                return dateString;
            }
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{TheDailyStarArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected String getFeaturedImageSelector() {
        return TheDailyStarArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return TheDailyStarArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return TheDailyStarArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        Elements articleDataBlocks = mDocument.select(TheDailyStarArticleParserInfo.ARTICLE_DATA_BLOCK_SELECTOR);

        if (articleDataBlocks!=null && articleDataBlocks.size() == 1) {
            Element articleDataBlock = articleDataBlocks.get(0);
            return articleDataBlock.select(TheDailyStarArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
        }
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return TheDailyStarArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return TheDailyStarArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return TheDailyStarArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return null;
    }
}
