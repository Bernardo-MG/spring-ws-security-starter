
package com.bernardomg.security.web.ws.error.test.config.controller;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public final class TestSecurityExceptionRequest {

    public static final RequestBuilder accessDenied() {
        return MockMvcRequestBuilders.get(SecurityExceptionController.PATH_ACCESS_DENIED_EXCEPTION)
            .contentType(MediaType.APPLICATION_JSON);
    }

    public static final RequestBuilder authenticationException() {
        return MockMvcRequestBuilders.get(SecurityExceptionController.PATH_AUTHENTICATION_EXCEPTION)
            .contentType(MediaType.APPLICATION_JSON);
    }

    private TestSecurityExceptionRequest() {
        super();
    }

}
