/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2022-2023 the original author or authors.
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

package com.bernardomg.security.springframework.web.jwt;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT token filter. Takes the JWT token from the request, validates it and initializes the authentication.
 * <h2>Header</h2>
 * <p>
 * The token should come in the Authorization header, which must follow a structure like this:
 * <p>
 * {@code Authorization: Bearer [token]}
 * <p>
 * This check is case insensitive.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class JwtTokenFilter extends OncePerRequestFilter {

    /**
     * Logger for the class.
     */
    private static final Logger             log = LoggerFactory.getLogger(JwtTokenFilter.class);

    /**
     * Handles authentication failures.
     */
    private final AuthenticationEntryPoint  authenticationEntryPoint;

    private final TokenAuthenticationParser tokenAuthenticationParser;

    private final TokenResolver             tokenResolver;

    /**
     * Constructs a filter with the received arguments.
     *
     * @param tokenResolv
     *            token resolver
     * @param tokenAuthenticationPars
     *            token authentication parser
     * @param authenticationEntry
     *            authentication failure entry point
     */
    public JwtTokenFilter(final TokenResolver tokenResolv, final TokenAuthenticationParser tokenAuthenticationPars,
            final AuthenticationEntryPoint authenticationEntry) {
        super();

        tokenResolver = Objects.requireNonNull(tokenResolv);
        tokenAuthenticationParser = Objects.requireNonNull(tokenAuthenticationPars);
        authenticationEntryPoint = Objects.requireNonNull(authenticationEntry,
            "The authentication entry point is required");
    }

    private void authenticate(final String token, final HttpServletRequest request) {
        final Authentication  authentication;
        final SecurityContext context;

        authentication = tokenAuthenticationParser.parse(token, request);

        context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        log.trace("Authenticated {} request for {} to {}", request.getMethod(), authentication.getName(),
            request.getServletPath());
    }

    private void handleAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
            final AuthenticationException exception) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        log.debug("JWT authentication failed for {} {}: {}", request.getMethod(), request.getServletPath(),
            exception.getMessage());

        authenticationEntryPoint.commence(request, response, exception);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {

        final Optional<String> token;

        log.trace("Authenticating {} request to {}", request.getMethod(), request.getServletPath());

        try {
            token = tokenResolver.resolve(request);

            if (token.isEmpty()) {
                log.trace("Missing authorization token");
            } else {
                authenticate(token.orElseThrow(), request);
            }
        } catch (final AuthenticationException ex) {
            handleAuthenticationFailure(request, response, ex);
        }

        chain.doFilter(request, response);
    }

}
