package com.dasbikash.news_server_parser.front_end_app;

import javafx.application.Application;
import javafx.stage.Stage;

public class ParserUiApp extends Application {
    @Override
    public void start(Stage window) throws Exception {

        window.setTitle("News Server Parser");
        window.setMinWidth(250);
        window.setMinHeight(250);

        setUserAgentStylesheet(STYLESHEET_CASPIAN);


        window.show();
    }
}
