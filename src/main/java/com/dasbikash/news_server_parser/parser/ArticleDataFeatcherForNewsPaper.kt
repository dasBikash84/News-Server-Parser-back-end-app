package com.dasbikash.news_server_parser.parser

import com.dasbikash.news_server_parser.model.Article
import com.dasbikash.news_server_parser.utils.DbSessionManager
import com.dasbikash.news_server_parser.model.EntityClassNames
import com.dasbikash.news_server_parser.model.Newspaper
import com.dasbikash.news_server_parser.model.Page
import com.dasbikash.news_server_parser.parser.article_body_parsers.ArticleBodyParserFactory
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParseRequest
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParser
import com.dasbikash.news_server_parser.parser.preview_page_parsers.PreviewPageParserFactory
import com.dasbikash.news_server_parser.utils.LoggerUtils
import org.hibernate.Session

import kotlin.collections.ArrayList

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

        dbSession = DbSessionManager.getNewSession()

        dbSession.persist(newspaper)

        getTopLevelPagesForNewsPaper(newspaper)
                .filter { it.active }
                .map { topLevelPages.add(it) }

        topLevelPages.forEach {
            childPageMap.put(it, ArrayList(getChildPageListForTopLevelPage(it)))
        }

        var maxChildPageCountForPage = 0

        childPageMap.values.forEach({
            if (maxChildPageCountForPage < it.size) {
                maxChildPageCountForPage = it.size
            }
        })

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
                    pageListForParsing.add(page)
                    LoggerUtils.consoleLog("NewsPaper: ${newspaper.name}, page: ${page.name}")
                }
            }
            i++
        }

        // So now here we have list of pages that need to be parsed for a certain newspaper
        do {
            pageListForParsing.forEach {

                println("Newspaper: ${it.newspaper?.name} ParentPage: ${it.parentPageId} Page Name: ${it.name}")

                val currentPageNumber: Int

                if (it.isPaginated()) {
                    currentPageNumber = getLastParsedPageNumber(it)
                } else {
                    currentPageNumber = NOT_APPLICABLE_PAGE_NUMBER
                }

                try {
                    waitForFareNetworkUsage()

                    val articleList = PreviewPageParser.parsePreviewPage(it, currentPageNumber)

                    articleList?.forEach {
                        it?.let {
                            dbSession.persist(it)
                            waitForFareNetworkUsage()
                            downloadAndSaveArticle(it)
                        }
                    }

                } catch (ex: Throwable) {
                    ex.printStackTrace()
                    LoggerUtils.dbErrorLog(ex.message)
                }
            }
        } while (true)
//        dbSession.close()
    }

    private fun downloadAndSaveArticle(article: Article) {
        val articleBodyParser = ArticleBodyParserFactory.getArticleBodyParserForArticle(article)
        var retryCount = 0
        do {
            val loadedArticle = articleBodyParser?.loadArticleBody(article)
            if (loadedArticle!!.isDownloaded()) {
                dbSession.save(loadedArticle)
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


    private fun getLastParsedPageNumber(it: Page) =
            lastParsedPageMap.get(it)?.let { it + 1 } ?: 1


    private fun getChildPageListForTopLevelPage(topLevelPage: Page): Collection<Page> {
        val childPageList = mutableListOf<Page>()

        val hql = "FROM ${EntityClassNames.PAGE} WHERE  parentPageId=:parentPageId and active=true"
        val query = dbSession.createQuery(hql)
        query.setParameter("parentPageId", topLevelPage.id)
        query.list().forEach { childPageList.add(it as Page) }

        return childPageList
    }

    private fun getTopLevelPagesForNewsPaper(newspaper: Newspaper): Collection<Page> {
        val topLevelPageList = mutableListOf<Page>()

        newspaper.pageList?.filter { it.isTopLevelPage() }?.map { topLevelPageList.add(it) }

        return topLevelPageList
    }
}
