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
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final Logger               log = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final TokenAuthenticationParser   tokenAuthenticationParser;

    private final TokenResolver               tokenResolver;

    private final AuthenticationTrustResolver trustResolver;

    /**
     * Constructs a filter with the received arguments.
     *
     * @param trustRes
     *            trust resolver
     * @param tokenResolv
     *            token resolver
     * @param tokenAuthenticationPars
     *            token authentication parser
     */
    public JwtTokenFilter(final AuthenticationTrustResolver trustRes, final TokenResolver tokenResolv,
            final TokenAuthenticationParser tokenAuthenticationPars) {
        super();

        trustResolver = Objects.requireNonNull(trustRes);
        tokenResolver = Objects.requireNonNull(tokenResolv);
        tokenAuthenticationParser = Objects.requireNonNull(tokenAuthenticationPars);
    }

    private final boolean isAuthenticated() {
        return ((SecurityContextHolder.getContext()
            .getAuthentication() != null) && !trustResolver.isAnonymous(
                SecurityContextHolder.getContext()
                    .getAuthentication()));
    }

    @Override
    protected final void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {
        final Optional<String>         token;
        final Optional<Authentication> authentication;

        log.trace("Authenticating {} request to {}", request.getMethod(), request.getServletPath());

        token = tokenResolver.resolve(request);

        if (token.isEmpty()) {
            // Missing header
            log.trace("Missing authorization token");
        } else if (!isAuthenticated()) {
            authentication = tokenAuthenticationParser.parse(token.get(), request);
            if (authentication.isEmpty()) {
                SecurityContextHolder.clearContext();
            } else {
                // User valid
                log.trace("Authenticated {} request for {} to {}", request.getMethod(), authentication.get()
                    .getName(), request.getServletPath());
                SecurityContextHolder.getContext()
                    .setAuthentication(authentication.get());
            }
        }

        chain.doFilter(request, response);
    }

}
