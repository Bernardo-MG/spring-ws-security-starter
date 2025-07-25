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

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.jwt.encoding.JwtTokenData;
import com.bernardomg.security.jwt.encoding.TokenEncoder;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

/**
 * JWT token encoder based on the JJWT library.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class JjwtTokenEncoder implements TokenEncoder {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(JjwtTokenEncoder.class);

    /**
     * Secret key for generating tokens.
     */
    private final SecretKey     key;

    /**
     * Constructs an encoder with the received arguments.
     *
     * @param secretKey
     *            secret key used for the token
     */
    public JjwtTokenEncoder(final SecretKey secretKey) {
        super();

        key = Objects.requireNonNull(secretKey, "The secret key must not be null");
    }

    @Override
    public final String encode(final JwtTokenData data) {
        final String     token;
        final Date       issuedAt;
        final Date       expiration;
        final Date       notBefore;
        final JwtBuilder jwtBuilder;

        jwtBuilder = Jwts.builder()
            .id(data.id())
            .issuer(data.issuer())
            .subject(data.subject());

        if (!data.permissions()
            .isEmpty()) {
            jwtBuilder.claim("permissions", data.permissions());
        }

        jwtBuilder.audience()
            .add(data.audience());

        // TODO: Use optional
        // Issued at
        if (data.issuedAt() != null) {
            issuedAt = java.util.Date.from(data.issuedAt()
                .atZone(ZoneId.systemDefault())
                .toInstant());
            jwtBuilder.issuedAt(issuedAt);
        }

        // Expiration
        if (data.expiration() != null) {
            expiration = java.util.Date.from(data.expiration()
                .atZone(ZoneId.systemDefault())
                .toInstant());
            jwtBuilder.expiration(expiration);
        }

        // Not before
        if (data.notBefore() != null) {
            notBefore = java.util.Date.from(data.notBefore()
                .atZone(ZoneId.systemDefault())
                .toInstant());
            jwtBuilder.notBefore(notBefore);
        }

        token = jwtBuilder.signWith(key, Jwts.SIG.HS512)
            .compact();

        log.debug("Created token from {}", data);

        return token;
    }

}
