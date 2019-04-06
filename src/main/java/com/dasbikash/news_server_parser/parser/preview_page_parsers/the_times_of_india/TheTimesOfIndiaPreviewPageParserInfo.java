package com.dasbikash.news_server_parser.parser.preview_page_parsers.the_times_of_india;

class TheTimesOfIndiaPreviewPageParserInfo {

    static final String ARTICLE_PREVIEW_BLOCK_SELECTOR[] = {"#content>li,.list5.clearfix>li,.top-newslist li",".md_news_box",".top-newslist li"};

    static final String ARTICLE_TITLE_ELEMENT_SELECTOR[] = {".w_tle","p",".w_tle"};

    static final String ARTICLE_LINK_ELEMENT_SELECTOR[] = {"a","a","a"};
    static final String ARTICLE_LINK_TEXT_SELECTOR_TAG[] = {"href","href","href"};

    static final String ARTICLE_PREVIEW_IMAGE_LINK_ELEMENT_SELECTOR[] = {"img","img","img"};
    static final String ARTICLE_PREVIEW_IMAGE_LINK_TEXT_SELECTOR_TAG[] = {"data-src","src","data-src"};

    static final String ARTICLE_PUBLICATION_DATE_ELEMENT_SELECTOR[] = {"[rodate]","[dateval]","[rodate]"};
    static final String ARTICLE_PUBLICATION_DATE_TEXT_SELECTOR_TAG[] = {"","dateval",""};

    static final String ARTICLE_PUBLICATION_DATE_TIME_FORMAT[] = {"dd MMM yyyy, HH:mm","dd MMM yyyy, HH:mm","dd MMM yyyy, HH:mm"};
}
