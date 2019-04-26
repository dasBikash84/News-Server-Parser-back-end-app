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

package com.dasbikash.news_server_parser.parser.preview_page_parsers;

import com.dasbikash.news_server_parser.exceptions.*;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

class PreviewPageParserTest {

    @Test
    public void testTT(){
        try {
            PreviewPageParser.parsePreviewPageForArticles(null,10);
        } catch (NewsPaperNotFoundForPageException | ParserNotFoundException e) {
            e.printStackTrace();
        } catch (PageLinkGenerationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (EmptyDocumentException e) {
            e.printStackTrace();
        } catch (EmptyArticlePreviewException e) {
            e.printStackTrace();
        }
    }

}