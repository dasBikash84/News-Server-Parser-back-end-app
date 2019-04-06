package com.dasbikash.news_server_parser.parser.preview_page_parsers;

import com.dasbikash.news_server_parser.model.Article;
import com.dasbikash.news_server_parser.model.Page;
import com.dasbikash.news_server_parser.parser.JsoupConnector;
import com.dasbikash.news_server_parser.utils.LinkProcessUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.*;

abstract public class PreviewPageParser {

    protected Page mCurrentPage;
    protected int mCurrentPageNumber;
    protected Document mDocument;
    protected String mPageLink;

    protected SimpleDateFormat mSimpleDateFormat = null;

    public static List<Article> parsePreviewPage(Page page,int pageNumber){

        if (page.getNewspaper() !=null){

            PreviewPageParser previewPageParser =
                    PreviewPageParserFactory.INSTANCE
                    .getPreviewLoaderByNewsPaper(page.getNewspaper());

            if (previewPageParser !=null){
                return previewPageParser.loadPreview(page,pageNumber);
            }
        }
        return null;
    }

    private List<Article> loadPreview(Page page, int pageNumber){

        mCurrentPage = page;//previewPageParseRequest.getPage();
        mCurrentPageNumber = pageNumber;//previewPageParseRequest.getPageNumber();

        System.out.println("mCurrentPage:"+mCurrentPage.getName());
        System.out.println("mCurrentPageNumber:"+mCurrentPageNumber);


        mPageLink = getPageLink();

        System.out.println("mPageLink: "+ mPageLink);

        if (mPageLink == null) {
            return null;
        }
        mDocument = JsoupConnector.INSTANCE.getDocument(mPageLink);

        if (mDocument == null) return null;

        System.out.println("Document title: "+ mDocument.title());

        return parseDocument();
    }

    private List<Article> parseDocument(){

        Elements mPreviewBlocks = getPreviewBlocks();

        if (mPreviewBlocks==null || mPreviewBlocks.size()==0){
            System.out.println("mPreviewBlocks==null || mPreviewBlocks.size()==0");
            return null;
        }

        List<Article> articles = new ArrayList<>();

        for (Element previewBlock: mPreviewBlocks){

            String articleLink;
            String previewImageLink;
            String articleTitle;
            Long articlePublicationDateTimeStamp = 0L;
            Long articleModificationDateTimeStamp = 0L;

            try {
                articleLink = getArticleLink(previewBlock);
                if (articleLink == null) continue;
                articleLink = processArticleLink(articleLink);
                System.out.println("articleLink: "+articleLink);
            } catch (Exception e) {
                continue;
            }

            try {
                previewImageLink = getArticlePreviewImageLink(previewBlock);
                previewImageLink = processArticlePreviewImageLink(previewImageLink);
                System.out.println("previewImageLink: "+previewImageLink);
            } catch (Exception e) {
                previewImageLink = null;
            }

            try {
                articleTitle = getArticleTitle(previewBlock);
                if (articleTitle == null) continue;
                System.out.println("articleTitle: "+articleTitle);
            } catch (Exception e) {
                continue;
            }
            if (getArticlePublicationDatetimeFormat() !=null) {
                mSimpleDateFormat = new SimpleDateFormat(getArticlePublicationDatetimeFormat());
                mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(mCurrentPage.getNewspaper().getCountry().getTimeZone()));
            }

            try {
                articlePublicationDateTimeStamp = getArticlePublicationTimeStamp(previewBlock);
                if (articlePublicationDateTimeStamp==null && mSimpleDateFormat !=null) {
                    articlePublicationDateTimeStamp = mSimpleDateFormat.parse(getArticlePublicationDateString(previewBlock)).getTime();
                }
                System.out.println("articlePublicationDateTimeStamp: "+articlePublicationDateTimeStamp);
            } catch (Exception e) {
                articlePublicationDateTimeStamp = 0L;
            }

            try {
                articleModificationDateTimeStamp = getArticleModificationTimeStamp(previewBlock);
                if (articleModificationDateTimeStamp==null && mSimpleDateFormat !=null) {
                    articleModificationDateTimeStamp = mSimpleDateFormat.parse(getArticleModificationDateString(previewBlock)).getTime();
                }
                System.out.println("articleModificationDateTimeStamp: "+articleModificationDateTimeStamp);
            } catch (Exception e) {
                articleModificationDateTimeStamp = 0L;
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
                            UUID.randomUUID().toString(),mCurrentPage,articleTitle,modificationDate,
                            publicationDate,null,new HashSet(),previewImageLink,articleLink
                    )
            );
        }
        return articles;
    }

    protected abstract String getArticlePublicationDatetimeFormat();

    protected abstract String getArticlePublicationDateString(Element previewBlock);

    protected abstract String getArticleTitle(Element previewBlock);

    protected abstract String getArticlePreviewImageLink(Element previewBlock);

    protected abstract String getArticleLink(Element previewBlock);

    protected abstract Elements getPreviewBlocks();



    private String getArticleModificationDateString(Element previewBlock){
        return null;
    }

    private Long getArticleModificationTimeStamp(Element previewBlock){
        return null;
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


    private String processLink(String linkText){
        if (linkText == null) return null;
        return LinkProcessUtils.processLink(linkText,getSiteBaseAddress());
    }

    protected abstract String getSiteBaseAddress();

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
}
