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
import org.apache.commons.lang3.StringUtils;

/**
 * Process the headers to set CamelHttpPath
 */
public class CamelHttpUriHeaderProcessor {

    /**
     * Process headers
     * @param exchange exchange
     * @return CamelHttpPath
     */
    public String process(Exchange exchange) {
        String basePath = exchange.getIn().getHeader("mailman_path", String.class);
        String threadPath = exchange.getIn().getHeader("mailman_thread_path", String.class);

        String camelHttpUri = basePath;
        if (StringUtils.isNotBlank(threadPath)) {
            camelHttpUri = basePath + threadPath;
        }

        return camelHttpUri;
    }
}
