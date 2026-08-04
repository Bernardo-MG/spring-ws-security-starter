
package com.bernardomg.security.springframework.web.jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenDecoder;
import com.bernardomg.security.springframework.model.ResourceActionGrantedAuthority;
import com.bernardomg.security.springframework.userdetails.SecurityUserDetails;

import jakarta.servlet.http.HttpServletRequest;

public final class TokenDetailsTokenAuthenticationParser implements TokenAuthenticationParser {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(TokenDetailsTokenAuthenticationParser.class);

    /**
     * Token decoder. Required to acquire the subject.
     */
    private final TokenDecoder  tokenDecoder;

    public TokenDetailsTokenAuthenticationParser(final TokenDecoder tokenDecoder) {
        super();

        this.tokenDecoder = Objects.requireNonNull(tokenDecoder);
    }

    @Override
    public final Optional<Authentication> parse(final String token, final HttpServletRequest request) {
        final Optional<Authentication> authentication;
        final JwtTokenData             tokenData;

        tokenData = tokenDecoder.decode(token);
        if ((tokenData.subject() == null) || tokenData.subject()
            .isBlank()) {
            log.debug("Missing JWT subject");
            throw new BadCredentialsException("JWT subject is missing");
        }

        if ((!tokenData.isExpired()) && (!tokenData.isBeforeStart())) {
            // Token not expired or for the future
            // Will load a new authentication from the token

            // Create and register authentication
            authentication = Optional.of(getAuthentication(request, tokenData));
        } else {
            log.trace("JWT validation failed");
            authentication = Optional.empty();
        }

        return authentication;
    }

    /**
     * Returns an {@link UsernamePasswordAuthenticationToken} created from the user and request.
     *
     * @param request
     *            request details for the authentication
     * @param tokenData
     *            parsed security token
     * @return an authentication object
     */
    private final Authentication getAuthentication(final HttpServletRequest request, final JwtTokenData tokenData) {
        final AbstractAuthenticationToken            authenticationToken;
        final Collection<? extends GrantedAuthority> authorities;
        final UserDetails                            userDetails;
        final Long                                   id;

        authorities = mapPermissions(tokenData.permissions());
        id = parseUserId(tokenData.values());
        // TODO: load all values
        userDetails = new SecurityUserDetails(id, "", tokenData.subject(), "", "", true, true, true, true, authorities);

        authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authenticationToken;
    }

    private Collection<? extends GrantedAuthority> mapPermissions(final Map<String, List<String>> permissions) {
        return permissions.entrySet()
            .stream()
            .flatMap(entry -> entry.getValue()
                .stream()
                .map(permission -> new ResourceActionGrantedAuthority(entry.getKey(), permission)))
            .toList();
    }

    private final Long parseUserId(final Map<String, String> values) {
        final String rawId;
        Long         id;

        rawId = values.get("id");

        if (rawId == null) {
            id = null;
        } else {
            try {
                id = Long.valueOf(rawId);
            } catch (final NumberFormatException ex) {
                id = null;
                log.debug("JWT id claim is invalid: {}", rawId);
                throw new BadCredentialsException("JWT id claim is invalid", ex);
            }
        }

        return id;
    }

}
