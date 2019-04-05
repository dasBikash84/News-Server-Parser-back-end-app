package com.dasbikash.news_server_parser.model.rest_end_points

data class RealTimeDbEndPoints(
        var rootSettingsNode: String = "",
        var countriesNode: String = "",
        var languagesNode: String = "",
        var newspapersNode: String = "",
        var pageGroupsNode: String = "",
        var pagesNode: String = "",
        var updateTimesNode: String = ""
)