/*
 * #%L
 * GarethHealy :: Postman Toppost Counter
 * %%
 * Copyright (C) 2013 - 2019 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.postmantoppostcounter.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Select thread links from Document via JSoup
 */
public class JSoupThreadLinksProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Document document = exchange.getIn().getMandatoryBody(Document.class);
        Element table = document.body().selectFirst("table");
        Elements authorLinks = table.select("a[href]:contains(thread)");

        exchange.getIn().setBody(authorLinks.eachAttr("href"));
    }
}
