package com.dasbikash.news_server_parser.parser

import com.dasbikash.news_server_parser.model.*
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.utils.DbSessionManager
import com.dasbikash.news_server_parser.utils.LoggerUtils
import org.hibernate.Session
import kotlin.random.Random

class ArticleDataFeatcherForNewsPaper(private val newspaper: Newspaper) : Runnable {


    private var lastNetworkRequestTS = 0L
    private val MIN_DELAY_BETWEEN_NETWORK_REQUESTS = 5000L;
    private val MAX_ARTICLE_DOWNLOAD_RETRY = 5
    private val NOT_APPLICABLE_PAGE_NUMBER = 0

    private val topLevelPages = mutableListOf<Page>()
    private val childPageMap = mutableMapOf<Page, ArrayList<Page>>() //HashMap<Page, List<Page>>()
    private val lastParsedPageMap = mutableMapOf<Page, Int>();
    private val donePageList = mutableListOf<Page>()
    lateinit var dbSession: Session

    override fun run() {

        //dbSession = DbSessionManager.getNewSession()
        getDatabaseSession().persist(newspaper)

        newspaper.pageList
                ?.asSequence()
                ?.filter { it.isTopLevelPage() }//status wiil be check prior to parsing
                ?.toCollection(topLevelPages)


        topLevelPages.asSequence()
                .forEach {
                    val topLevelPage = it
                    val childPageList = ArrayList<Page>()
                    newspaper.pageList
                            ?.filter { it.parentPageId == topLevelPage.id }
                            ?.toCollection(childPageList)
                    childPageMap.put(it, childPageList)
                }

        var maxChildPageCountForPage = 0

        childPageMap.values.forEach({
            if (maxChildPageCountForPage < it.size) {
                maxChildPageCountForPage = it.size
            }
        }) //get maximum child count

        val pageListForParsing = mutableListOf<Page>()

        //add active top level pages to parcable page list
        topLevelPages.forEach {
            if (it.linkFormat != null) { //if only point to page
                pageListForParsing.add(it)
            }
        }

        var i = 0

        while (i < maxChildPageCountForPage) {
            childPageMap.values.forEach {
                if (it.size > i) {
                    val page = it.get(i)
                    if (page.linkFormat != null) {
                        pageListForParsing.add(page)
                    }
                }
            }
            i++
        }

        getDatabaseSession().close()

        //pageListForParsing.forEach { println("Page name: ${it.name} Page id: ${it.id} Parent id: ${it.parentPageId}") }

        // So now here we have list of pages that need to be parsed for a certain newspaper

        //var count = 3;

        do {
            val tempPageList = ArrayList<Page>()
            tempPageList.addAll(pageListForParsing)


            println("#############################################################################################")
            println("#############################################################################################")
            println("Going to parse page:")

            do {
                val itemIndexForRemoval = Random(System.currentTimeMillis()).nextInt(tempPageList.size)
                val currentPage = tempPageList.get(itemIndexForRemoval)
                tempPageList.removeAt(itemIndexForRemoval)
                println("Page Name: ${currentPage.name} Page Id: ${currentPage.id} ParentPage: ${currentPage.parentPageId} Newspaper: ${currentPage.newspaper?.name}")

                val currentPageNumber: Int

                if (currentPage.isPaginated()) {
                    currentPageNumber = getLastParsedPageNumber(currentPage)+1
                } else {
                    currentPageNumber = NOT_APPLICABLE_PAGE_NUMBER
                }
                //currentPageNumber +=1

                try {
                    waitForFareNetworkUsage()

                    val articleList = PreviewPageParser.parsePreviewPageForArticles(currentPage, currentPageNumber)

                    val parcelableArticleList = mutableListOf<Article>()

                    articleList.forEach {

                        if(!getDatabaseSession().transaction.isActive) {
                            getDatabaseSession().beginTransaction()
                        }
                        try {
                            getDatabaseSession().save(it)
                            parcelableArticleList.add(it)
                            getDatabaseSession().transaction.commit()
                        } catch (ex: Throwable) {
                            ex.printStackTrace()
                            val stackTrace = mutableListOf<StackTraceElement>()
                            ex.stackTrace.toCollection(stackTrace)
                            if(!getDatabaseSession().transaction.isActive) {
                                getDatabaseSession().beginTransaction()
                            }
                            LoggerUtils.dbErrorLog("Message: ${ex.message} Cause: ${ex.cause?.message} StackTrace: ${stackTrace}", getDatabaseSession())
                            getDatabaseSession().transaction.commit()
                        }
                    }

                    if(!getDatabaseSession().transaction.isActive) {
                        getDatabaseSession().beginTransaction()
                    }

                    getDatabaseSession().save(PageParsingHistory(page = currentPage,pageNumber = currentPageNumber,articleCount = parcelableArticleList.size))
                    getDatabaseSession().transaction.commit()

                } catch (ex: Throwable) {
                    ex.printStackTrace()
                    val stackTrace = mutableListOf<StackTraceElement>()
                    ex.stackTrace.toCollection(stackTrace)
                    if(!getDatabaseSession().transaction.isActive) {
                        getDatabaseSession().beginTransaction()
                    }
                    LoggerUtils.dbErrorLog("Message: ${ex.message} Cause: ${ex.cause?.message} StackTrace: ${stackTrace}", getDatabaseSession())
                    getDatabaseSession().transaction.commit()
                }

            } while (tempPageList.size > 0)
            println("#############################################################################################")
            println("#############################################################################################")
        } while (true)
    }

    private fun downloadAndSaveArticle(article: Article) {
        var retryCount = 0
        do {
            val loadedArticle = ArticleBodyParser.getArticleBody(article)
            if (loadedArticle!!.isDownloaded()) {
                dbSession!!.save(loadedArticle)
                break
            }
            waitForFareNetworkUsage()
            retryCount++
        } while (retryCount < MAX_ARTICLE_DOWNLOAD_RETRY)
    }

    fun waitForFareNetworkUsage() {
        while ((System.currentTimeMillis() - lastNetworkRequestTS) < MIN_DELAY_BETWEEN_NETWORK_REQUESTS);
        lastNetworkRequestTS = System.currentTimeMillis()
    }


    private fun getLastParsedPageNumber(page: Page): Int {
        val query = getDatabaseSession().createQuery("FROM PageParsingHistory where pageId=:currentPageId order by created desc")
        query.setParameter("currentPageId",page.id)
        try {
            val historyEntry = query.list().first() as PageParsingHistory
            return historyEntry.pageNumber
        } catch (ex: Exception) {
            return 0
        }
    }

    private fun getDatabaseSession(): Session {
        if (!::dbSession.isInitialized || /*!dbSession.isConnected || */!dbSession.isOpen) {
            dbSession = DbSessionManager.getNewSession()
        }
        return dbSession
    }


    /*private fun getChildPageListForTopLevelPage(topLevelPage: Page): Collection<Page> {
        val childPageList = mutableListOf<Page>()

        val hql = "FROM ${EntityClassNames.PAGE} WHERE  parentPageId=:parentPageId and active=true"
        val query = dbSession!!.createQuery(hql)
        query.setParameter("parentPageId", topLevelPage.id)
        query.list().forEach { childPageList.add(it as Page) }

        return childPageList
    }

    private fun getTopLevelPagesForNewsPaper(newspaper: Newspaper): Collection<Page> {
        val topLevelPageList = mutableListOf<Page>()

        newspaper.pageList?.filter { it.isTopLevelPage() }?.map { topLevelPageList.add(it) }

        return topLevelPageList
    }*/
}
