package com.dasbikash.news_server_parser.parser.article_body_parsers;

import com.dasbikash.news_server_parser.model.Article;
import com.dasbikash.news_server_parser.model.ArticleImage;
import com.dasbikash.news_server_parser.model.Country;
import com.dasbikash.news_server_parser.parser.JsoupConnector;
import com.dasbikash.news_server_parser.utils.LinkProcessUtils;
import com.dasbikash.news_server_parser.utils.ToDoUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

abstract public class ArticleBodyParser {

    private static final String ARTICLE_IMAGE_BLOCK_REMOVER_REGEX = "<img.+?>";
    private static final String ARTICLE_IMAGE_BLOCK_REPLACER_REGEX = "";

    private static final int DEFAULT_REQUIRED_FEATURED_IMAGE_COUNT = 1;
    private static final int DEFAULT_FEATURED_IMAGE_INDEX = 0;

    protected final ArrayList<String> mParagraphInvalidatorText =
            new ArrayList<>();

    protected final ArrayList<String> mUnwantedArticleText =
            new ArrayList<>();

    protected final ArrayList<String> mParagraphQuiterText =
            new ArrayList<>();

    {
        mParagraphInvalidatorText.add("আরও পড়ুন");
        mParagraphInvalidatorText.add("আরও পড়ুন");
        mParagraphInvalidatorText.add("Read |");
        mParagraphInvalidatorText.add("READ |");
        mParagraphInvalidatorText.add("READ More |");
        mParagraphInvalidatorText.add("Read More |");
        mParagraphInvalidatorText.add("Also Read");
        mParagraphInvalidatorText.add("Also read");
        mParagraphInvalidatorText.add("Read Also");
        mParagraphInvalidatorText.add("Read also");
        mUnwantedArticleText.add("&nbsp;");
        mUnwantedArticleText.add("\\<div.+?\\>\\<\\/div\\>");
        mUnwantedArticleText.add("READ\\s+?ALSO.+?\\</a\\>");
        mUnwantedArticleText.add("<br>");
    }

    protected Document mDocument;
    protected Article mArticle;
    private StringBuilder mArticleTextBuilder = new StringBuilder();

    protected abstract String[] getArticleModificationDateStringFormats();
    protected abstract String getArticleModificationDateString();

    protected abstract String getSiteBaseAddress();

    protected abstract Elements getArticleFragmentBlocks();

    protected abstract String getFeaturedImageCaptionSelectorAttr();
    protected abstract String getParagraphImageCaptionSelector();
    protected abstract String getParagraphImageCaptionSelectorAttr();
    protected abstract String getParagraphImageLinkSelectorAttr();
    protected abstract String getParagraphImageSelector();
    protected abstract String  getFeaturedImageSelector();
    protected abstract String getFeaturedImageLinkSelectorAttr();

    protected abstract String getFeaturedImageCaptionSelector();


    protected int getReqFeaturedImageCount(){
        return DEFAULT_REQUIRED_FEATURED_IMAGE_COUNT;
    }
    protected int getReqFeaturedImageIndex(){ return DEFAULT_FEATURED_IMAGE_INDEX;}

    protected String processLink(String linkText){
        String siteBaseAddress = getSiteBaseAddress();
        return LinkProcessUtils.processLink(linkText,siteBaseAddress);
    }

    public static Article loadArticleBody(Article article){

        if (article.isDownloaded()){
            return article;
        }
        System.out.println("Article not downloaded.So, work needed to be done.");

        if (article.getArticleLink() !=null){
            ArticleBodyParser articleBodyParser =
                    ArticleBodyParserFactory.INSTANCE.getArticleBodyParserForArticle(article);
            if (articleBodyParser!=null){
                return articleBodyParser.parseArticleBody(article);
            }
        }
        return null;
    }

    private Article parseArticleBody(Article article){

        //ToDoUtils.workToDo();
        System.out.println("Start of parsing");

        mArticle = article;

        mDocument = JsoupConnector.INSTANCE.getDocument(mArticle.getArticleLink());

        if (mDocument == null) {
            return mArticle;
        }

        System.out.println("Article document title: "+mDocument.title());

        if (getArticleModificationDateStringFormats()!= null){

            String modificationDateString = getArticleModificationDateString();

            if (modificationDateString !=null && modificationDateString.length()>0){

                Country country = mArticle.getPage().getNewspaper().getCountry();
                Calendar articleModificationTime = Calendar.getInstance(TimeZone.getTimeZone(country.getTimeZone()));
                articleModificationTime.setTimeInMillis(0L);
                SimpleDateFormat simpleDateFormat;

                String[] modificationDateStringFormats = getArticleModificationDateStringFormats();

                for (int i = 0; i < modificationDateStringFormats.length; i++) {
                    try {
                        simpleDateFormat = new SimpleDateFormat(modificationDateStringFormats[i]);
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(country.getTimeZone()));
                        articleModificationTime.setTime(simpleDateFormat.parse(modificationDateString));
                        if (articleModificationTime.getTimeInMillis() !=0L) {
                            mArticle.setModificationTs(articleModificationTime.getTimeInMillis());
                            break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (getFeaturedImageSelector()!=null) {

            try{
                Elements featuredImageElements = mDocument.select(getFeaturedImageSelector());
                //Log.d(TAG, "parseArticle: mArticle.getTitle():"+mArticle.getTitle());
                //Log.d(TAG, "parseArticle: featuredImageElements.size():"+featuredImageElements.size());

                if (featuredImageElements!=null && featuredImageElements.size() == getReqFeaturedImageCount()) {
                    Element featuredImage = featuredImageElements.get(getReqFeaturedImageIndex());
                    //Log.d(TAG, "parseArticle:featuredImageLink: "+featuredImage.html());
                    if (getFeaturedImageLinkSelectorAttr() !=null) {

                        String featuredImageLink = featuredImage.attr(getFeaturedImageLinkSelectorAttr());
                        //Log.d(TAG, "parseArticle:featuredImageLink: "+featuredImageLink);
                        if (featuredImageLink.trim().length() > 0) {
                            featuredImageLink = processLink(featuredImageLink);
                            //Log.d(TAG, "parseArticle:featuredImageLink: "+featuredImageLink);

                            String imageCaption = "";
                            try {
                                if (getFeaturedImageCaptionSelectorAttr() !=null) {
                                    //Log.d(TAG, "parseArticle:getFeaturedImageCaptionSelectorAttr() !=null:  "+getFeaturedImageCaptionSelectorAttr());
                                    imageCaption = featuredImage.attr(getFeaturedImageCaptionSelectorAttr());
                                    //Log.d(TAG, "parseArticle:imageCaption from attr: "+imageCaption);
                                } else if(getFeaturedImageCaptionSelector() !=null) {
                                    //Log.d(TAG, "parseArticle: getFeaturedImageCaptionSelector() !=null");

                                    Elements featuredImageCaptionElements = mDocument.select(getFeaturedImageCaptionSelector());
                                    if (featuredImageCaptionElements.size()>0){
                                        imageCaption = featuredImageCaptionElements.get(getReqFeaturedImageIndex()).text();
                                        //Log.d(TAG, "parseArticle:imageCaption: "+imageCaption);
                                    }
                                }
                            } catch (Exception ex){
                                //Log.d(TAG, "parseArticle: Error: "+ex.getMessage());
                                ex.printStackTrace();
                            }
                            //Log.d(TAG, "parseArticle: ");

                            mArticle.getImageLinkList().add(new ArticleImage(featuredImageLink,imageCaption));

                           /* int featuredImageDataId =
                                    ImageDataHelper.saveImageData(
                                            featuredImageLink.hashCode(),
                                            featuredImageLink,
                                            imageCaption
                                    );
                            ArticleFragmentPayload articleFragmentPayload = ArticleFragmentPayloadHelper.getInstanceByImageDataId(featuredImageDataId);
                            if (articleFragmentPayload != null) {
                                mArticleFragmentPayloads.add(articleFragmentPayload);
                            }*/
                        }
                    }
                }

            } catch (Exception ex){
                //Log.d(TAG, "parseArticle: Error: "+ex.getMessage());
                ex.printStackTrace();
            }
        }

        Elements articleFragments = getArticleFragmentBlocks();//mDocument.select(getArticleFragmentBlockSelector());

        if (articleFragments!=null && articleFragments.size()>0) {

            articleFragmentLooper:

            for (Element articleFragment :
                    articleFragments) {

                String paraText = articleFragment.html();

                if (paraText.trim().length() == 0) continue;
                //Log.d(TAG, "parseArticle: paraText:"+paraText);

                if (getParagraphImageSelector() !=null) {

                    try {

                        Elements imageChildren = articleFragment.select(getParagraphImageSelector());
                        boolean imageCaptionFound = false;

                        if (imageChildren.size() > 0) {

                            for (Element imageChild :
                                    imageChildren) {

                                if (getParagraphImageLinkSelectorAttr() == null ||
                                        getParagraphImageLinkSelectorAttr().trim().length() == 0) continue;

                                String articleImageLink = imageChild.attr(getParagraphImageLinkSelectorAttr());

                                if (articleImageLink.length() > 0) {
                                    articleImageLink = processLink(articleImageLink);
                                    String imageCaption = "";
                                    try {

                                        if (getParagraphImageCaptionSelectorAttr() != null) {
                                            imageCaption = imageChild.attr(getParagraphImageCaptionSelectorAttr());
                                        } else if (getParagraphImageCaptionSelector() != null) {
                                            Elements imageCaptionElements = articleFragment.select(getParagraphImageCaptionSelector());
                                            if (imageCaptionElements.size() > 0) {
                                                imageCaption = imageCaptionElements.get(0).text();
                                                imageCaptionFound = true;
                                            }
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    mArticle.getImageLinkList().add(new ArticleImage(articleImageLink,imageCaption));

                                    /*int articleImageDataId =
                                            ImageDataHelper.saveImageData(
                                                    articleImageLink.hashCode(),
                                                    articleImageLink,
                                                    imageCaption
                                            );

                                    ArticleFragmentPayload articleFragmentPayload = ArticleFragmentPayloadHelper.getInstanceByImageDataId(articleImageDataId);

                                    if (articleFragmentPayload != null) {
                                        mArticleFragmentPayloads.add(articleFragmentPayload);
                                    }*/
                                }
                            }
                        }
                        if (imageCaptionFound) {
                            //Log.d(TAG, "parseArticle: imageCaptionFound");
                            continue;
                        }
                        paraText = paraText.replaceAll(
                                ARTICLE_IMAGE_BLOCK_REMOVER_REGEX,
                                ARTICLE_IMAGE_BLOCK_REPLACER_REGEX
                        );
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                if (paraText.trim().length()==0) continue;

                for (String paragraphQuiterText:
                        mParagraphQuiterText) {
                    if (paraText.contains(paragraphQuiterText)) break articleFragmentLooper;
                }

                for (String paragraphInvalidatorText:
                        mParagraphInvalidatorText) {
                    if (paraText.contains(paragraphInvalidatorText)) continue articleFragmentLooper;
                }

                for (String unwantedArticleText:
                        mUnwantedArticleText) {
                    paraText = paraText.replaceAll(unwantedArticleText,"");
                }

                mArticleTextBuilder.append(paraText+"<br>");

                //int textDataId = TextDataHelper.saveTextData(paraText);

                /*ArticleFragmentPayload articleFragmentPayload = ArticleFragmentPayloadHelper.getInstanceByTextDataId(textDataId);

                if (articleFragmentPayload != null) {
                    mArticleFragmentPayloads.add(articleFragmentPayload);
                }*/
            }
        }

        mArticle.setArticleText(mArticleTextBuilder.toString());

        return mArticle;
    }
}
