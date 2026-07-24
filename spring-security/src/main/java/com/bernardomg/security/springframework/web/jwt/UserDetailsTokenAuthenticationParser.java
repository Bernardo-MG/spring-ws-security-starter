
package com.bernardomg.security.springframework.web.jwt;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenDecoder;

import jakarta.servlet.http.HttpServletRequest;

public final class UserDetailsTokenAuthenticationParser implements TokenAuthenticationParser {

    /**
     * Logger for the class.
     */
    private static final Logger      log = LoggerFactory.getLogger(UserDetailsTokenAuthenticationParser.class);

    /**
     * Token decoder. Required to acquire the subject.
     */
    private final TokenDecoder       tokenDecoder;

    /**
     * User details service. Gives access to the user, to validate the token against it.
     */
    private final UserDetailsService userDetailsService;

    public UserDetailsTokenAuthenticationParser(final TokenDecoder tokenDecoder,
            final UserDetailsService userDetailsService) {
        super();

        this.tokenDecoder = Objects.requireNonNull(tokenDecoder);
        this.userDetailsService = Objects.requireNonNull(userDetailsService);
    }

    @Override
    public final Optional<Authentication> parse(final String token, final HttpServletRequest request) {
        final String                   username;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;
        final JwtTokenData             tokenData;

        tokenData = tokenDecoder.decode(token);
        if ((!tokenData.isExpired()) && (!tokenData.isBeforeStart())) {
            // Token not expired
            // Will load a new authentication from the token

            // Takes subject from the token
            username = tokenData.subject();
            userDetails = userDetailsService.loadUserByUsername(username);

            if (isValid(userDetails)) {
                // Create and register authentication
                authentication = Optional.of(getAuthentication(userDetails, request, token));

            } else {
                log.trace("Invalid user {}", username);
                authentication = Optional.empty();
            }
        } else {
            log.trace("JWT validation failed");
            authentication = Optional.empty();
        }

        return authentication;
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

        authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authenticationToken;
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

}
