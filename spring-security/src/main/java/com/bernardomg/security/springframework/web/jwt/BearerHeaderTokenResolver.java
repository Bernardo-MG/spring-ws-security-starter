
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

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
        Pattern.CASE_INSENSITIVE);

    /**
     * Logger for the class.
     */
    private static final Logger  log                  = LoggerFactory.getLogger(JwtTokenFilter.class);

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
        } else {
            // Security header received
            // Check for the token
            // TODO: Should be case insensitive
            matcher = authorizationPattern.matcher(header);
            if (!matcher.matches()) {
                log.debug("Malformed token");
                throw new BadCredentialsException("Malformed Authorization header");
            }
            token = Optional.ofNullable(matcher.group("token"));
        }

        return token;
    }

}
