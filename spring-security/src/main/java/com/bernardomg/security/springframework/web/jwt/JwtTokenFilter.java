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
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenDecoder;

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
    private static final Logger               log                     = LoggerFactory.getLogger(JwtTokenFilter.class);

    /**
     * Token header identifier. This is added before the token to tell which kind of token it is. Used to make sure the
     * authentication header is valid.
     */
    private static final String               TOKEN_HEADER_IDENTIFIER = "Bearer";

    /**
     * Token decoder. Required to acquire the subject.
     */
    private final TokenDecoder                tokenDecoder;

    private final AuthenticationTrustResolver trustResolver;

    /**
     * User details service. Gives access to the user, to validate the token against it.
     */
    private final UserDetailsService          userDetailsService;

    /**
     * Constructs a filter with the received arguments.
     *
     * @param userDetService
     *            user details service
     * @param decoder
     *            token decoder
     * @param trustRes
     *            trust resolver
     */
    public JwtTokenFilter(final UserDetailsService userDetService, final TokenDecoder decoder,
            final AuthenticationTrustResolver trustRes) {
        super();

        userDetailsService = Objects.requireNonNull(userDetService);
        tokenDecoder = Objects.requireNonNull(decoder);
        trustResolver = Objects.requireNonNull(trustRes);
    }

    /**
     * Returns an {@link UsernamePasswordAuthenticationToken} created from the user and request.
     *
     * @param userDetails
     *            user for the authentication
     * @param request
     *            request details for the authentication
     * @param token
     *            parsed security token
     * @return an authentication object
     */
    private final Authentication getAuthentication(final UserDetails userDetails, final HttpServletRequest request,
            final String token) {
        final AbstractAuthenticationToken authenticationToken;

        authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authenticationToken;
    }

    /**
     * Takes the token from the authorization header. This is expected to be something like
     * {@code Authorization: Bearer [token]}.
     *
     * @param request
     *            request containing the header with the token
     * @return the token if found, or an empty {@code Optional} otherwise
     */
    private final Optional<String> getToken(final HttpServletRequest request) {
        final String           header;
        final Optional<String> token;

        header = request.getHeader("Authorization");

        if (header == null) {
            // No token received
            token = Optional.empty();
            log.trace("Missing authorization header, can't return token");
        } else if (((header != null) && !header.trim()
            .isEmpty()) && (header.trim()
                .startsWith(TOKEN_HEADER_IDENTIFIER + " "))) {
            // Token received
            // Take it by removing the identifier
            // TODO: Should be case insensitive
            token = Optional.of(header.substring(TOKEN_HEADER_IDENTIFIER.length())
                .trim());
        } else {
            // Invalid token received
            token = Optional.empty();
            log.trace("Authorization header {} has an invalid structure, can't return token", header);
        }

        return token;
    }

    /**
     * Checks if the user is valid. This means it has no flag marking it as not usable.
     *
     * @param userDetails
     *            user the check
     * @return {@code true} if the user is valid, {@code false} otherwise
     */
    private final boolean isValid(final UserDetails userDetails) {
        return userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked()
                && userDetails.isCredentialsNonExpired() && userDetails.isEnabled();
    }

    private final void loadToken(final String token, final HttpServletRequest request) {
        final String         username;
        final UserDetails    userDetails;
        final Authentication authentication;
        final JwtTokenData   tokenData;

        tokenData = tokenDecoder.decode(token);
        if (!tokenData.isExpired()) {
            // Token not expired
            // Will load a new authentication from the token

            // Takes subject from the token
            username = tokenDecoder.decode(token)
                .subject();
            userDetails = userDetailsService.loadUserByUsername(username);

            if (isValid(userDetails)) {
                // Create and register authentication
                authentication = getAuthentication(userDetails, request, token);
                SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

                // User valid
                log.trace("Authenticated {} request for {} to {}", request.getMethod(), username,
                    request.getServletPath());
            } else {
                log.trace("Invalid user {}", username);
            }
        } else {
            log.trace("JWT validation failed");
        }
    }

    @Override
    protected final void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {
        final Optional<String> token;

        log.trace("Authenticating {} request to {}", request.getMethod(), request.getServletPath());

        token = getToken(request);

        if (token.isEmpty()) {
            // Missing header
            log.trace("Missing authorization token");
        } else if ((SecurityContextHolder.getContext()
            .getAuthentication() == null) || (trustResolver.isAnonymous(
                SecurityContextHolder.getContext()
                    .getAuthentication()))) {
            // Not authenticated
            loadToken(token.get(), request);
        }

        chain.doFilter(request, response);
    }

}
