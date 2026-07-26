
package com.bernardomg.security.springframework.web.jwt;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenAuthenticationParser {

    public Optional<Authentication> parse(final String token, final HttpServletRequest request);

}
