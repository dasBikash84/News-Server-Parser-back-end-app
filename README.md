# Article data parser for News-Server project
Cack-end console application for Article data parsering of NewsServer Project. The app is written in Kotlin and mysql 
database is used for data storage. This app is designed to run continuously in back-end server in order to parse latest 
article data from designated News-paper web sites and store in database. Then the parsed article data (and also newspaer settings data)
is presented to [`coordinator`](https://github.com/dasBikash84/news_server_data_coordinator) app via a [`rest service`](https://github.com/dasBikash84/news_server_parser_rest_end_point).

Key classes and respective functionality of this app are as below:

* [`DataParsingCoordinator`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/kotlin/com/dasbikash/news_server_parser/DataParsingCoordinator.kt)
: This is application entry point and it also performes coordination of Article Data parser classes for individual 
newspapers. It runs an infinite loop and operates Article Data parsers as per news-paper activation status.

* [`BootStrapSettings`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/kotlin/com/dasbikash/news_server_parser/bootstrap/BootStrapSettings.kt)
: Class for initial bootstarap of app settings from local json file.

* [`ArticleDataFeatcherForNewsPaper`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/kotlin/com/dasbikash/news_server_parser/parser/ArticleDataFeatcherForNewsPaper.kt)
: Inherited from Thread for parsing article data of designated newspaper in back-ground. Seperated instances are ceated from this class
for each active newspaper. It runs a infinite loop and within that loop scanes through respective newspaper pages periodically 
for newly published articles.

* [`JsoupConnector`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/kotlin/com/dasbikash/news_server_parser/parser/JsoupConnector.kt)
: Singleton to fetch parseble jsoup document from page link.

* [`PreviewPageParser`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/java/com/dasbikash/news_server_parser/parser/preview_page_parsers/PreviewPageParser.java)
: Base abstract class for Article preview page parsing.

* [`PreviewPageParserFactory`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/kotlin/com/dasbikash/news_server_parser/parser/preview_page_parsers/PreviewPageParserFactory.kt)
: Singleton factory for generating Article preview page parser implementation for given News-paper.

* [`ArticleBodyParser`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/java/com/dasbikash/news_server_parser/parser/article_body_parsers/ArticleBodyParser.java)
:Base abstract class for Article body content parsing.

* [`ArticleBodyParserFactory`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/kotlin/com/dasbikash/news_server_parser/parser/article_body_parsers/ArticleBodyParserFactory.kt)
: Singleton factory for generating Article body content parser implementation for given News-paper.

* [`DatabaseUtils`](https://github.com/dasBikash84/ns_reloaded_backend_Parser_/blob/master/src/main/kotlin/com/dasbikash/news_server_parser/database/DatabaseUtils.kt)
: Singleton ulility class for database I/O.
