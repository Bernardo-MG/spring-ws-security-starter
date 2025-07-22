/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.web.ws.error.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.bernardomg.security.web.ws.error.config.SecurityExceptionHandlerTestConfig;
import com.bernardomg.security.web.ws.error.config.controller.SecurityExceptionController;
import com.bernardomg.security.web.ws.error.config.controller.TestSecurityExceptionRequest;
import com.bernardomg.test.TestApplication;

@WebMvcTest(SecurityExceptionController.class)
@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SecurityExceptionHandlerTestConfig.class)
@ContextConfiguration(classes = TestApplication.class)
@DisplayName("Security exception handler")
class ITSecurityExceptionHandler {

    @Autowired
    private MockMvc mockMvc;

    public ITSecurityExceptionHandler() {
        super();
    }

    @Test
    @DisplayName("With an access denied exception it returns an error response")
    void testErrorHandling_AccessDenied() throws Exception {
        final ResultActions result;

        // WHEN
        result = mockMvc.perform(TestSecurityExceptionRequest.accessDenied())
            .andDo(MockMvcResultHandlers.print());

        // THEN

        // The value was not found
        result.andExpect(status().isUnauthorized());

        // TODO: return the correct response

        // The response contains the expected attributes
        // result.andExpect(jsonPath("$.code", equalTo("401")));
        // result.andExpect(jsonPath("$.message", equalTo("Bad request")));

        // The response contains no content field
        // result.andExpect(jsonPath("$.content").doesNotExist());

        // The response contains no failures attribute
        // result.andExpect(jsonPath("$.failures").doesNotExist());
    }

    @Test
    @DisplayName("With an authentication exception it returns an error response")
    void testErrorHandling_AuthenticationException() throws Exception {
        final ResultActions result;

        // WHEN
        result = mockMvc.perform(TestSecurityExceptionRequest.authenticationException());

        // THEN

        // The value was not found
        result.andExpect(status().isUnauthorized());

        // TODO: return the correct response

        // The response contains the expected attributes
        // result.andExpect(jsonPath("$.code", equalTo("401")));
        // result.andExpect(jsonPath("$.message", equalTo("Bad request")));

        // The response contains no content field
        // result.andExpect(jsonPath("$.content").doesNotExist());

        // The response contains no failures attribute
        // result.andExpect(jsonPath("$.failures").doesNotExist());
    }

}
