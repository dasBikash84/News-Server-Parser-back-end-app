package com.dasbikash.news_server_parser.parser.article_body_parsers.jugantor;

import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser;
import com.dasbikash.news_server_parser.utils.DisplayUtils;
import org.jsoup.select.Elements;

public class JugantorArticleParser extends ArticleBodyParser {

    //private static final String TAG = "StackTrace";
    private static final String TAG = "JugArticleLoader";

    private final String mSiteBaseAddress = "https://www.jugantor.com";

    @Override
    protected String getSiteBaseAddress() {
        return mSiteBaseAddress;
    }

    @Override
    protected String processLink(String linkText) {
        if (linkText.matches("^\\./.+")){
            linkText = linkText.substring(1);
        }
        return super.processLink(linkText);
    }

    @Override
    protected String getArticleModificationDateString() {

        Elements dateStringElements =
                mDocument.select(JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_SELECTOR);

        if (dateStringElements != null && dateStringElements.size()==1) {

            String dateString = dateStringElements.get(0).text().trim();

            dateString = DisplayUtils.banglaToEnglishDateString(dateString);

            dateString = dateString.replaceAll(JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_SELECTOR,
                    JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_CLEANER_REPLACEMENT).trim();
            return dateString;
        }
        return null;
    }

    @Override
    protected String[] getArticleModificationDateStringFormats() {
        return new String[]{JugantorArticleParserInfo.ARTICLE_MODIFICATION_DATE_STRING_FORMATS};
    }

    @Override
    protected String getFeaturedImageSelector() {
        return JugantorArticleParserInfo.FEATURED_IMAGE_SELECTOR;
    }

    @Override
    protected String getFeaturedImageLinkSelectorAttr() {
        return JugantorArticleParserInfo.FEATURED_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getFeaturedImageCaptionSelector() {
        return JugantorArticleParserInfo.FEATURED_IMAGE_CAPTION_SELECTOR;
    }

    @Override
    protected String getFeaturedImageCaptionSelectorAttr() {
        return null;
    }

    @Override
    protected Elements getArticleFragmentBlocks() {
        return mDocument.select(JugantorArticleParserInfo.ARTICLE_FRAGMENT_BLOCK_SELECTOR);
    }

    @Override
    protected String getParagraphImageCaptionSelector() {
        return null;
    }

    @Override
    protected String getParagraphImageSelector() {
        return JugantorArticleParserInfo.PARAGRAPH_IMAGE_SELECTOR;
    }

    @Override
    protected String getParagraphImageLinkSelectorAttr() {
        return JugantorArticleParserInfo.PARAGRAPH_IMAGE_LINK_SELECTOR_ATTR;
    }

    @Override
    protected String getParagraphImageCaptionSelectorAttr() {
        return JugantorArticleParserInfo.PARAGRAPH_IMAGE_CAPTION_SELECTOR_ATTR;
    }
}
