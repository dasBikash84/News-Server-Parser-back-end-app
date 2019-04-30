/*
 * Copyright 2019 das.bikash.dev@gmail.com. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasbikash.news_server_parser.parser.preview_page_parsers;

import com.dasbikash.news_server_parser.exceptions.*;
import com.dasbikash.news_server_parser.model.Article;
import com.dasbikash.news_server_parser.model.Page;
import com.dasbikash.news_server_parser.parser.JsoupConnector;
import com.dasbikash.news_server_parser.utils.HashUtils;
import com.dasbikash.news_server_parser.utils.LinkProcessUtils;
import kotlin.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract public class PreviewPageParser {

    protected Page mCurrentPage;
    protected int mCurrentPageNumber;
    protected Document mDocument;
    protected String mPageLink;

    protected SimpleDateFormat mSimpleDateFormat = null;

    protected abstract String getArticlePublicationDatetimeFormat();
    protected abstract String getArticlePublicationDateString(Element previewBlock);
    protected abstract String getArticleTitle(Element previewBlock);
    protected abstract String getArticlePreviewImageLink(Element previewBlock);
    protected abstract String getArticleLink(Element previewBlock);
    protected abstract Elements getPreviewBlocks();
    protected abstract String getSiteBaseAddress();

    public static Pair<List<Article>,String> parsePreviewPageForArticles(Page page, int pageNumber)
            throws NewsPaperNotFoundForPageException, ParserNotFoundException, PageLinkGenerationException,
            URISyntaxException, EmptyDocumentException, EmptyArticlePreviewException {

        if (page.getNewspaper() ==null){
            throw new NewsPaperNotFoundForPageException(page);
        }

        PreviewPageParser previewPageParser = PreviewPageParserFactory.INSTANCE
                                                        .getPreviewPageParserByNewsPaper(page.getNewspaper());

        if (previewPageParser ==null){
            throw new ParserNotFoundException(page.getNewspaper());
        }

        return previewPageParser.getArticlePreviews(page,pageNumber);
    }

    private Pair<List<Article>,String> getArticlePreviews(Page page, int pageNumber)
            throws PageLinkGenerationException, URISyntaxException,
            EmptyDocumentException, EmptyArticlePreviewException {

        mCurrentPage = page;
        mCurrentPageNumber = pageNumber;

        System.out.println("mCurrentPage:"+mCurrentPage.getName());
        System.out.println("mCurrentPageNumber:"+mCurrentPageNumber);


        mPageLink = getPageLink();

        System.out.println("mPageLink: "+ mPageLink);

        if (mPageLink == null) {
            throw new PageLinkGenerationException(page);
        }
        mDocument = JsoupConnector.INSTANCE.getDocument(mPageLink);

        if (mDocument == null){
            //noinspection SingleStatementInBlock
            throw new EmptyDocumentException("Np: "+mCurrentPage.getNewspaper().getName()+", Page: "+mCurrentPage.getName()+
                                                ", Link: "+mPageLink);
        }

        System.out.println("Document title: "+ mDocument.title());

        return parseDocument();
    }

    private Pair<List<Article>,String> parseDocument() throws URISyntaxException, EmptyArticlePreviewException {

        Elements mPreviewBlocks = getPreviewBlocks();

        if (mPreviewBlocks==null || mPreviewBlocks.size()==0){
            throw new EmptyArticlePreviewException("Np: "+mCurrentPage.getNewspaper().getName()+", Page: "+mCurrentPage.getName()+
                                                    ", Link: "+mPageLink+" before parsing");
        }

        List<Article> articles = new ArrayList<>();
        StringBuilder parsingLogMessage = new StringBuilder("For page: "+mPageLink+" ");

        int articleCount = 0;

        for (Element previewBlock: mPreviewBlocks){

            String articleLink;
            String previewImageLink;
            String articleTitle;
            Long articlePublicationDateTimeStamp = 0L;
            Long articleModificationDateTimeStamp = 0L;
            articleCount++;
            parsingLogMessage.append("For article num ").append(articleCount).append(": ");
            try {
                articleLink = getArticleLink(previewBlock);
                if (articleLink == null) continue;
                articleLink = processArticleLink(articleLink);
                System.out.println("articleLink: "+articleLink);
            } catch (Exception e) {
                parsingLogMessage.append("articleLink == null");
                e.printStackTrace();
                continue;
            }

            try {
                previewImageLink = getArticlePreviewImageLink(previewBlock);
                previewImageLink = processArticlePreviewImageLink(previewImageLink);
                System.out.println("previewImageLink: "+previewImageLink);
            } catch (Exception e) {
                previewImageLink = null;
                e.printStackTrace();
                parsingLogMessage.append("previewImageLink = null");
            }

            try {
                articleTitle = getArticleTitle(previewBlock);
                if (articleTitle == null) continue;
                System.out.println("articleTitle: "+articleTitle);
            } catch (Exception e) {
                parsingLogMessage.append("articleTitle = null");
                e.printStackTrace();
                continue;
            }
            if (getArticlePublicationDatetimeFormat() !=null) {
                mSimpleDateFormat = new SimpleDateFormat(getArticlePublicationDatetimeFormat());
                mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(mCurrentPage.getNewspaper().getCountry().getTimeZone()));
            }

            try {
                articlePublicationDateTimeStamp = getArticlePublicationTimeStamp(previewBlock);
                if (articlePublicationDateTimeStamp==null && mSimpleDateFormat !=null) {
                    System.out.println("mSimpleDateFormat.toPattern(): "+mSimpleDateFormat.toPattern());
                    articlePublicationDateTimeStamp = mSimpleDateFormat.parse(getArticlePublicationDateString(previewBlock)).getTime();
                }
                System.out.println("articlePublicationDateTimeStamp: "+articlePublicationDateTimeStamp);
            } catch (Exception e) {
                articlePublicationDateTimeStamp = 0L;
                e.printStackTrace();
                parsingLogMessage.append("Publication TimeStamp not found");
            }

            try {
                articleModificationDateTimeStamp = getArticleModificationTimeStamp(previewBlock);
                if (articleModificationDateTimeStamp==null && mSimpleDateFormat !=null) {
                    articleModificationDateTimeStamp = mSimpleDateFormat.parse(getArticleModificationDateString(previewBlock)).getTime();
                    parsingLogMessage.append("Modification TimeStamp found");
                }
                System.out.println("articleModificationDateTimeStamp: "+articleModificationDateTimeStamp);
            } catch (Exception e) {
                articleModificationDateTimeStamp = 0L;
//                e.printStackTrace();
            }

            Date publicationDate = null;
            Date modificationDate = null;

            if (articlePublicationDateTimeStamp !=null && articlePublicationDateTimeStamp !=0L){
                Calendar publicationTS = Calendar.getInstance();
                publicationTS.setTimeInMillis(articlePublicationDateTimeStamp);
                publicationDate = publicationTS.getTime();
            }

            if (articleModificationDateTimeStamp !=null && articleModificationDateTimeStamp !=0L){
                Calendar modificationTS = Calendar.getInstance();
                modificationTS.setTimeInMillis(articlePublicationDateTimeStamp);
                modificationDate = modificationTS.getTime();
            }

            articles.add(
                    new Article(
                            HashUtils.INSTANCE.hash(articleLink).toString(),mCurrentPage,articleTitle,modificationDate,
                            publicationDate,null,null,new ArrayList<>(),previewImageLink,articleLink
                    )
            );
        }

        if (articles.size() == 0){
            throw new EmptyArticlePreviewException("Np: "+mCurrentPage.getNewspaper().getName()+", Page: "+mCurrentPage.getName()+
                                                    ", Link: "+mPageLink+" after parsing");
        }

        //noinspection unchecked
        return new Pair(articles,parsingLogMessage.toString());
    }
    @SuppressWarnings("MagicConstant")
    protected String getPageLink(){

        if (mCurrentPage == null || mCurrentPage.getLinkFormat() == null){
            System.out.println("mCurrentPage == null || mCurrentPage.getLinkFormat() == null");
            return null;
        }

        if (mCurrentPage.getLinkVariablePartFormat() == null){
            System.out.println("mCurrentPage.getLinkVariablePartFormat() == null");
            return mCurrentPage.getLinkFormat();
        }

        if (mCurrentPage.getLinkVariablePartFormat().equals(Page.DEFAULT_LINK_TRAILING_FORMAT)){
            System.out.println("mCurrentPage.getLinkVariablePartFormat().equals(Page.DEFAULT_LINK_TRAILING_FORMAT)");
            return mCurrentPage.getLinkFormat().replace(mCurrentPage.getLinkVariablePartFormat(),""+mCurrentPageNumber);
        } else {

            //Newspaper newspaper = NewspaperHelper.findNewspaperById(feature.getNewsPaperId());
            if (mCurrentPage.getNewspaper() == null) return null;

//            Country country = c
            if (mCurrentPage.getNewspaper().getCountry() == null) return null;

            Calendar currentTime = Calendar.getInstance();
            currentTime.setTimeZone(TimeZone.getTimeZone(mCurrentPage.getNewspaper().getCountry().getTimeZone()));

            if (mCurrentPage.getWeekly()){
                do {
                    if (mCurrentPage.getWeeklyPublicationDay() == currentTime.get(Calendar.DAY_OF_WEEK)) {
                        break;
                    }
                    currentTime.add(Calendar.DAY_OF_MONTH, -1);
                } while (true);
                currentTime.add(Calendar.DAY_OF_YEAR,-1*(mCurrentPageNumber-1)*7);
            } else {
                currentTime.add(Calendar.DAY_OF_YEAR, -1 * (mCurrentPageNumber - 1));
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mCurrentPage.getLinkVariablePartFormat());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(mCurrentPage.getNewspaper().getCountry().getTimeZone()));

            return mCurrentPage.getLinkFormat().replace(
                    mCurrentPage.getLinkVariablePartFormat(),
                    simpleDateFormat.format(currentTime.getTime())
            );
        }

    }

    private String getArticleModificationDateString(Element previewBlock){
        return null;
    }
    protected Long getArticleModificationTimeStamp(Element previewBlock){
        return null;
    }
    private String processLink(String linkText){
        if (linkText == null) return null;
        return LinkProcessUtils.processLink(linkText,getSiteBaseAddress());
    }

    protected Long getArticlePublicationTimeStamp(Element previewBlock){
        return null;
    }
    protected String processArticlePreviewImageLink(String previewImageLink){
        return processLink(previewImageLink);
    }
    protected String processArticleLink(String articleLink){
        return processLink(articleLink);
    }
}
