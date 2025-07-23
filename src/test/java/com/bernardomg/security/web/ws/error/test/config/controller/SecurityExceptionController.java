
package com.bernardomg.security.web.ws.error.test.config.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SecurityExceptionController.PATH)
public class SecurityExceptionController {

    public static final String PATH                          = "/exception/security";

    public static final String PATH_ACCESS_DENIED_EXCEPTION  = PATH + "/accdenied";

    public static final String PATH_AUTHENTICATION_EXCEPTION = PATH + "/auth";

    public SecurityExceptionController() {
        super();
    }

    @GetMapping(path = "/accdenied", produces = MediaType.APPLICATION_JSON_VALUE)
    public void accessDenied() {
        throw new AccessDeniedException("Access denied");
    }

    @GetMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public void authenticationException() {
        throw new BadCredentialsException("Authentication failure");
    }

}
