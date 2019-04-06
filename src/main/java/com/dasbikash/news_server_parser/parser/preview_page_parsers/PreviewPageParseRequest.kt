package com.dasbikash.news_server_parser.parser.preview_page_parsers

import com.dasbikash.news_server_parser.model.Page

data class PreviewPageParseRequest(
        val page:Page,var pageNumber:Int= NOT_APPLICABLE_PAGE_NUMBER
){
    companion object {
        @JvmField
        val NOT_APPLICABLE_PAGE_NUMBER = -1
    }
    fun ifPageNumberApplicable():Boolean{
        return pageNumber != NOT_APPLICABLE_PAGE_NUMBER
    }
}
