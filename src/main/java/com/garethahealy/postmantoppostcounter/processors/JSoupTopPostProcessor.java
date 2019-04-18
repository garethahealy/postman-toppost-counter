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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Select top posters from Document via JSoup
 */
public class JSoupTopPostProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Document document = exchange.getIn().getMandatoryBody(Document.class);
        Elements posts = document.body().select("body > ul");

        //NOTE: Probably a way to do this via selectors, but didnt work it out
        List<String> topPosters = new ArrayList<>();
        for (Element parent : posts.last().children()) {
            if (parent.is("li")) {
                for (Element children : parent.children()) {
                    if (children.is("em")) {
                        topPosters.add(children.text());
                    }
                }
            }
        }

        Map<String, Long> cardsMap = topPosters.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        exchange.getIn().setBody(cardsMap);
    }
}
