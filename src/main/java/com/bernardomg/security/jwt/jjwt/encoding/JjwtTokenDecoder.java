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

package com.bernardomg.security.jwt.jjwt.encoding;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.SecretKey;

import com.bernardomg.security.jwt.encoding.JwtTokenData;
import com.bernardomg.security.jwt.encoding.TokenDecoder;

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

    @SuppressWarnings("unchecked")
    @Override
    public final JwtTokenData decode(final String token) {
        final Claims                    claims;
        final LocalDateTime             issuedAt;
        final LocalDateTime             expiration;
        final LocalDateTime             notBefore;
        final Map<String, List<String>> permissions;

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

        // Permissions
        if (claims.get("permissions") != null) {
            permissions = (Map<String, List<String>>) claims.get("permissions");
        } else {
            permissions = null;
        }

        return JwtTokenData.builder()
            .withId(claims.getId())
            .withSubject(claims.getSubject())
            .withAudience(claims.getAudience())
            .withIssuer(claims.getIssuer())
            .withIssuedAt(issuedAt)
            .withExpiration(expiration)
            .withNotBefore(notBefore)
            .withPermissions(permissions)
            .build();
    }

}
