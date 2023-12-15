
package com.bernardomg.security.authentication.jwt.token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import javax.crypto.SecretKey;

import com.bernardomg.security.authentication.jwt.token.model.JwtTokenData;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

/**
 * JWT token decoder based on the JJWT library.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class JjwtTokenDecoder implements TokenDecoder {

    /**
     * JWT parser for reading tokens.
     */
    private final JwtParser parser;

    /**
     * Builds a decoder with the received parser.
     *
     * @param prsr
     *            JWT parser
     */
    public JjwtTokenDecoder(final JwtParser prsr) {
        super();

        parser = Objects.requireNonNull(prsr);
    }

    /**
     * Builds a decoder with the received key.
     *
     * @param key
     *            secret key for the token
     */
    public JjwtTokenDecoder(final SecretKey key) {
        super();

        Objects.requireNonNull(key);

        parser = Jwts.parser()
            .verifyWith(key)
            .build();
    }

    @Override
    public final JwtTokenData decode(final String token) {
        final Claims        claims;
        final LocalDateTime issuedAt;
        final LocalDateTime expiration;
        final LocalDateTime notBefore;

        // Acquire claims
        claims = parser.parseSignedClaims(token)
            .getPayload();

        // Issued at
        if (claims.getIssuedAt() != null) {
            issuedAt = claims.getIssuedAt()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        } else {
            issuedAt = null;
        }

        // Expiration
        if (claims.getExpiration() != null) {
            expiration = claims.getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        } else {
            expiration = null;
        }

        // Not before
        if (claims.getNotBefore() != null) {
            notBefore = claims.getNotBefore()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        } else {
            notBefore = null;
        }

        // TODO: And the permissions?
        return JwtTokenData.builder()
            .withId(claims.getId())
            .withSubject(claims.getSubject())
            .withAudience(claims.getAudience())
            .withIssuer(claims.getIssuer())
            .withIssuedAt(issuedAt)
            .withExpiration(expiration)
            .withNotBefore(notBefore)
            .build();
    }

}
