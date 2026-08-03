
package com.bernardomg.security.springframework.web.jwt;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.servlet.http.HttpServletRequest;

public final class BearerHeaderTokenResolver implements TokenResolver {

    private static final Pattern authorizationPattern    = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
        Pattern.CASE_INSENSITIVE);

    /**
     * Logger for the class.
     */
    private static final Logger  log                     = LoggerFactory.getLogger(JwtTokenFilter.class);

    private static final String  TOKEN_HEADER_IDENTIFIER = "Bearer";

    public BearerHeaderTokenResolver() {
        super();
    }

    @Override
    public final Optional<String> resolve(final HttpServletRequest request) {
        final String           header;
        final Optional<String> token;
        final Matcher          matcher;

        header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null) {
            // No token received
            token = Optional.empty();
            log.trace("Missing authorization header, can't return token");
        } else if (header.startsWith(TOKEN_HEADER_IDENTIFIER)) {
            // Token received
            // Take it by removing the identifier
            // TODO: Should be case insensitive
            matcher = authorizationPattern.matcher(header);
            if (!matcher.matches()) {
                log.debug("Malformed token");
                throw new BadCredentialsException("Malformed Authorization header");
            }
            token = Optional.ofNullable(matcher.group("token"));
        } else {
            // Invalid token received
            token = Optional.empty();
            log.trace("Authorization header {} has an invalid structure, can't return token", header);
        }

        return token;
    }

}
