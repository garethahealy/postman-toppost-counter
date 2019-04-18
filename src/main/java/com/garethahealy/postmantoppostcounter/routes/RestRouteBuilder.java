/*-
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
package com.garethahealy.postmantoppostcounter.routes;

import java.util.Map;
import javax.ws.rs.core.MediaType;

import com.garethahealy.postmantoppostcounter.processors.CamelHttpUriHeaderProcessor;
import com.garethahealy.postmantoppostcounter.processors.JSoupParserProcessor;
import com.garethahealy.postmantoppostcounter.processors.JSoupThreadLinksProcessor;
import com.garethahealy.postmantoppostcounter.processors.JSoupTopPostProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

/**
 * Main route builder
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration()
                .contextPath("/camel")
                .apiContextRouteId("swagger")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "REST API")
                .apiProperty("api.version", "v1")
                .component("servlet")
                .bindingMode(RestBindingMode.auto);

        // @formatter:off
        rest().id("count-rest")
                .description("Count the number of top posters")
                .get("/count")
                .param()
                    .name("mailman_path")
                    .description("Path to download mailman archive from")
                    .required(true)
                    .type(RestParamType.query)
                .endParam()
                .responseMessage()
                    .code(200)
                    .responseModel(Map.class)
                .endResponseMessage()
                .route()
                    .id("count")
                    .log("Counting for; ${header.mailman_path}")
                    .to("direct:downloadHtmlPage")
                    .to("direct:processArchivePage")
                    .split().body()
                        .setHeader("mailman_thread_path").simple("${body}")
                        .to("direct:downloadHtmlPage")
                        .to("direct:processThreadPage")
                        .log("${header.mailman_thread_path}; ${body}")
                    .end()
                .end()
                .endRest();

        from("direct:downloadHtmlPage").id("downloadHtmlPage")
                .removeHeaders("Camel*")
                .setBody().constant(null)
                .setHeader(Exchange.HTTP_METHOD).constant("GET")
                .setHeader(Exchange.HTTP_PATH).method(new CamelHttpUriHeaderProcessor(), "process")
                .log("Attempting to download html page for; https://post-office.corp.redhat.com/${header.CamelHttpPath}")
                .to("http4:post-office.corp.redhat.com/")
                .convertBodyTo(String.class);

        from("direct:processArchivePage").id("processArchivePage")
                .log("Processing archive page...")
                .process(new JSoupParserProcessor())
                .process(new JSoupThreadLinksProcessor());

        from("direct:processThreadPage").id("processThreadPage")
                .log("Processing thread page...")
                .process(new JSoupParserProcessor())
                .process(new JSoupTopPostProcessor());
        // @formatter:on
    }
}
