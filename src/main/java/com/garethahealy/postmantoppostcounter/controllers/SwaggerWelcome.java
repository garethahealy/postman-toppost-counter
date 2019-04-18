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
package com.garethahealy.postmantoppostcounter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller swagger redirect
 */
@Controller
public class SwaggerWelcome {

    /**
     * Swagger direct
     * @return Swagger direct
     */
    @RequestMapping("/swagger-ui")
    public String redirectToUi() {
        return "redirect:/webjars/swagger-ui/index.html?url=/camel/api-doc&validatorUrl=";
    }
}
