
package com.bernardomg.security.springframework.web.jwt;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenAuthenticationParser {

    public Authentication parse(final String token, final HttpServletRequest request);

}
