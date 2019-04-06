package com.dasbikash.news_server_parser.parser.preview_page_parser;

import com.dasbikash.news_server_parser.model.Article;
import com.dasbikash.news_server_parser.utils.ToDoUtils;

import java.util.List;

abstract public class PreviewPageParser {

    abstract public List<Article> loadPreview(PreviewPageParseRequest previewPageParseRequest);
}
