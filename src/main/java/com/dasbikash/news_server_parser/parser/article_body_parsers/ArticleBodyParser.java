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

package com.dasbikash.news_server_parser.parser.article_body_parsers;

import com.dasbikash.news_server_parser.exceptions.*;
import com.dasbikash.news_server_parser.model.Article;
import com.dasbikash.news_server_parser.model.ArticleImage;
import com.dasbikash.news_server_parser.model.Country;
import com.dasbikash.news_server_parser.parser.JsoupConnector;
import com.dasbikash.news_server_parser.utils.LinkProcessUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

abstract public class ArticleBodyParser {

    private static final String ARTICLE_IMAGE_BLOCK_REMOVER_REGEX = "<img.+?>";
    private static final String ARTICLE_IMAGE_BLOCK_REPLACER_REGEX = "";

    private static final int DEFAULT_REQUIRED_FEATURED_IMAGE_COUNT = 1;
    private static final int DEFAULT_FEATURED_IMAGE_INDEX = 0;
    protected static final int FEATURED_IMAGE_COUNT_FOR_MUILIPLE_IMAGE = -1;

    protected final ArrayList<String> mParagraphInvalidatorText =
            new ArrayList<>();

    private final ArrayList<String> mUnwantedArticleText =
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

    protected abstract String getParagraphImageSelector();

    protected abstract String getParagraphImageLinkSelectorAttr();

    protected abstract String getParagraphImageCaptionSelector();

    protected abstract String getParagraphImageCaptionSelectorAttr();

    protected abstract String getFeaturedImageSelector();

    protected abstract String getFeaturedImageLinkSelectorAttr();

    protected abstract String getFeaturedImageCaptionSelector();

    protected abstract String getFeaturedImageCaptionSelectorAttr();


    protected int getReqFeaturedImageCount() {
        return DEFAULT_REQUIRED_FEATURED_IMAGE_COUNT;
    }

    protected int getReqFeaturedImageIndex() {
        return DEFAULT_FEATURED_IMAGE_INDEX;
    }

    protected String processLink(String linkText) {
        String siteBaseAddress = getSiteBaseAddress();
        return LinkProcessUtils.INSTANCE.processLink(linkText, siteBaseAddress);
    }

    public static void getArticleBody(Article article)
            throws EmptyArticleLinkException, ParserNotFoundException, EmptyJsoupDocumentException,
            EmptyArticleBodyException, URISyntaxException, ArticleModificationTimeNotFoundException {

        if (article.isDownloaded()) return;
        //System.out.println("Article not downloaded.So, work needed to be done.");

        if (article.getArticleLink() == null) throw new EmptyArticleLinkException(article);

        ArticleBodyParser articleBodyParser =
                ArticleBodyParserFactory.INSTANCE.getArticleBodyParserForArticle(article);
        if (articleBodyParser == null)
            throw new ParserNotFoundException(Objects.requireNonNull(article.getPage().getNewspaper()));

        articleBodyParser.mArticle = article;

        articleBodyParser.mDocument = JsoupConnector.INSTANCE.getDocument(Objects.requireNonNull(articleBodyParser.mArticle.getArticleLink()));

        if (articleBodyParser.mDocument == null) {
            throw new EmptyJsoupDocumentException("For article: " + articleBodyParser.mArticle.getArticleLink());
        }

        articleBodyParser.parseArticleBody();
    }

    public static void getArticleBody(Article article, String articlePageContent) throws
            EmptyArticleLinkException, ParserNotFoundException, EmptyJsoupDocumentException,
            ArticleModificationTimeNotFoundException, EmptyArticleBodyException, URISyntaxException {

        if (article.isDownloaded()) return;

        if (article.getArticleLink() == null) throw new EmptyArticleLinkException(article);

        ArticleBodyParser articleBodyParser =
                ArticleBodyParserFactory.INSTANCE.getArticleBodyParserForArticle(article);
        if (articleBodyParser == null)
            throw new ParserNotFoundException(Objects.requireNonNull(article.getPage().getNewspaper()));

        articleBodyParser.mArticle = article;

        articleBodyParser.mDocument = Jsoup.parse(articlePageContent, articleBodyParser.mArticle.getArticleLink());

        if (articleBodyParser.mDocument == null) {
            throw new EmptyJsoupDocumentException("For article: " + articleBodyParser.mArticle.getArticleLink());
        }

        articleBodyParser.parseArticleBody();
    }

    private void parseArticleBody() throws URISyntaxException, EmptyJsoupDocumentException, EmptyArticleBodyException, ArticleModificationTimeNotFoundException {

        if (mArticle.getPublicationTS() == null && getArticleModificationDateStringFormats() != null) {

            String modificationDateString = getArticleModificationDateString();

            if (modificationDateString != null && modificationDateString.length() > 0) {

                Country country = mArticle.getPage().getNewspaper().getCountry();
                Calendar articleModificationTime = Calendar.getInstance(TimeZone.getTimeZone(country.getTimeZone()));
                articleModificationTime.setTimeInMillis(0L);
                SimpleDateFormat simpleDateFormat;

                String[] modificationDateStringFormats = getArticleModificationDateStringFormats();

                int i = 0;

                for (; i < modificationDateStringFormats.length; i++) {
                    try {
                        simpleDateFormat = new SimpleDateFormat(modificationDateStringFormats[i]);
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(country.getTimeZone()));
                        articleModificationTime.setTime(simpleDateFormat.parse(modificationDateString));
                        if (articleModificationTime.getTimeInMillis() != 0L) {
                            mArticle.setModificationTs(articleModificationTime.getTimeInMillis());
                            break;
                        }
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    }
                }
                if (i == modificationDateStringFormats.length) {
                    throw new ArticleModificationTimeNotFoundException(mArticle);
                }
            }
        }

        if (getFeaturedImageSelector() != null) {

            try {
                Elements featuredImageElements = mDocument.select(getFeaturedImageSelector());
                //System.out.println("featuredImageElements.size(): "+featuredImageElements.size());
                if (featuredImageElements != null) {
                    if (getReqFeaturedImageCount() != FEATURED_IMAGE_COUNT_FOR_MUILIPLE_IMAGE) {
                        if (featuredImageElements.size() == getReqFeaturedImageCount()) {
                            Element featuredImage = featuredImageElements.get(getReqFeaturedImageIndex());
                            if (getFeaturedImageLinkSelectorAttr() != null) {

                                String featuredImageLink = featuredImage.attr(getFeaturedImageLinkSelectorAttr());
                                if (featuredImageLink.trim().length() > 0) {
                                    featuredImageLink = processLink(featuredImageLink);

                                    String imageCaption = "";
                                    try {
                                        if (getFeaturedImageCaptionSelectorAttr() != null) {
                                            imageCaption = featuredImage.attr(getFeaturedImageCaptionSelectorAttr());
                                        } else if (getFeaturedImageCaptionSelector() != null) {

                                            Elements featuredImageCaptionElements = mDocument.select(getFeaturedImageCaptionSelector());
                                            if (featuredImageCaptionElements.size() > 0) {
                                                imageCaption = featuredImageCaptionElements.get(getReqFeaturedImageIndex()).text();
                                            }
                                        }
                                    } catch (Exception ex) {}
                                    mArticle.getImageLinkList().add(new ArticleImage(featuredImageLink, imageCaption));
                                }
                            }
                        }
                    }else {
                        for (Element featuredImage : featuredImageElements){
                            if (getFeaturedImageLinkSelectorAttr() != null) {

                                String featuredImageLink = featuredImage.attr(getFeaturedImageLinkSelectorAttr());
                                if (featuredImageLink.trim().length() > 0) {
                                    featuredImageLink = processLink(featuredImageLink);

                                    String imageCaption = "";
                                    try {
                                        if (getFeaturedImageCaptionSelectorAttr() != null) {
                                            imageCaption = featuredImage.attr(getFeaturedImageCaptionSelectorAttr());
                                        } else if (getFeaturedImageCaptionSelector() != null) {
                                            imageCaption = mDocument.select(getFeaturedImageCaptionSelector()).get(featuredImageElements.indexOf(featuredImage)).text();
                                        }
                                    } catch (Exception ex) {}
                                    mArticle.getImageLinkList().add(new ArticleImage(featuredImageLink, imageCaption));
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        }

        Elements articleFragments = getArticleFragmentBlocks();

        if (articleFragments != null && articleFragments.size() > 0) {

            articleFragmentLooper:

            for (Element articleFragment :
                    articleFragments) {
                String paraText = articleFragment.html();
                if (paraText.trim().length() == 0) continue;

                if (getParagraphImageSelector() != null) {

                    try {
                        Elements imageChildren = articleFragment.select(getParagraphImageSelector());
                        boolean imageCaptionFound = false;
                        if (imageChildren.size() > 0) {

                            for (Element imageChild :
                                    imageChildren) {

                                if (getParagraphImageLinkSelectorAttr() == null ||
                                        getParagraphImageLinkSelectorAttr().trim().length() == 0) continue;

                                String articleImageLink = imageChild.attr(getParagraphImageLinkSelectorAttr());

                                if (articleImageLink.length() > 0 && articleImageLink.length() < 1000) {
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
//                                        ex.printStackTrace();
                                    }
//                                    System.out.println("Article Image found: "+articleImageLink);
                                    mArticle.getImageLinkList().add(new ArticleImage(articleImageLink, imageCaption));
                                }
                            }
                        }
                        if (imageCaptionFound) {
                            continue;
                        }
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    }
                }

                paraText = paraText.replaceAll(
                        ARTICLE_IMAGE_BLOCK_REMOVER_REGEX,
                        ARTICLE_IMAGE_BLOCK_REPLACER_REGEX
                );

                if (paraText.trim().length() == 0) continue;

                for (String paragraphQuiterText :
                        mParagraphQuiterText) {
                    if (paraText.contains(paragraphQuiterText)) break articleFragmentLooper;
                }

                for (String paragraphInvalidatorText :
                        mParagraphInvalidatorText) {
                    if (paraText.contains(paragraphInvalidatorText)) continue articleFragmentLooper;
                }

                for (String unwantedArticleText :
                        mUnwantedArticleText) {
                    paraText = paraText.replaceAll(unwantedArticleText, "");
                }
//                System.out.println("New Para: "+paraText);
                mArticleTextBuilder.append(paraText + "<br><br>");

            }
        }

        if (mArticleTextBuilder.toString().isEmpty()) {
            throw new EmptyArticleBodyException(mArticle);
        }

        mArticle.setArticleText(mArticleTextBuilder.toString());

//        System.out.println("Article text before processing: "+mArticle.getArticleText());
        //noinspection ConstantConditions
        mArticle.setArticleText(mArticle.getArticleText().replaceAll("<a.+?>", "").replace("</a>", ""));
        mArticle.setArticleText(mArticle.getArticleText().trim());
//        System.out.println("Article text after processing: "+mArticle.getArticleText());
    }
}
