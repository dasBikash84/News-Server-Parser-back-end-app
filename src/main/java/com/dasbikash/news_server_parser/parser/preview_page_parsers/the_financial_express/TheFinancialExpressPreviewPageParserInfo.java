package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_financial_express;

class TheFinancialExpressPreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR = ".card,.equal-cards-wrapper .local-news,.equal-cards-wrapper1 .local-news";

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR2 = ".equal-cards-wrapper .local-news,.equal-cards-wrapper1 .local-news";

    static final String ARTICLE_LINK_ELEMENT_SELECTOR[] = {"h3 a",""};
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG[] = {"href","href"};

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR = "img";
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG = "data-src";

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR[] = {"h3","h4"};
}
