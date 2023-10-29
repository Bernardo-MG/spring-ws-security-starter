/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.mvc.error.test.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bernardomg.mvc.error.test.util.controller.ErrorTestController;
import com.bernardomg.test.config.annotation.MvcIntegrationTest;

@MvcIntegrationTest
@AutoConfigureMockMvc
@DisplayName("Controller error handling - argument validation")
class ITControllerArgumentError {

    @Autowired
    private MockMvc mockMvc;

    public ITControllerArgumentError() {
        super();
    }

    private final RequestBuilder getMethodArgumentRequest() {
        return MockMvcRequestBuilders.post(ErrorTestController.PATH_METHOD_ARG)
            .content("{}")
            .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Returns the response structure for method argument errors")
    void testErrorHandling_MethodArgumentError_Response() throws Exception {
        final ResultActions result;

        result = mockMvc.perform(getMethodArgumentRequest());

        // The operation was rejected
        result.andExpect(MockMvcResultMatchers.status()
            .isBadRequest());

        // The response contains the expected attributes
        result.andExpect(MockMvcResultMatchers.jsonPath("$.failures", Matchers.aMapWithSize(1)));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.failures", Matchers.hasKey("name")));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.failures['name']", Matchers.hasSize(1)));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.failures['name'][0].field", Matchers.equalTo("name")));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.failures['name'][0].value", Matchers.equalTo(null)));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.failures['name'][0].code", Matchers.equalTo("empty")));
        result.andExpect(
            MockMvcResultMatchers.jsonPath("$.failures['name'][0].message", Matchers.equalTo("must not be null")));

        // The response contains no content field
        result.andExpect(MockMvcResultMatchers.jsonPath("$.content")
            .doesNotExist());
    }

}
