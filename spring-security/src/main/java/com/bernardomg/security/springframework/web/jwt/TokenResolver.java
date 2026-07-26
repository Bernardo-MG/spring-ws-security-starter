
package com.bernardomg.security.springframework.web.jwt;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenResolver {

    public Optional<String> resolve(final HttpServletRequest request);

}
