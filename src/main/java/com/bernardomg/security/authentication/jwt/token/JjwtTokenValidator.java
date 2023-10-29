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

package com.bernardomg.security.authentication.jwt.token;

import java.time.LocalDateTime;

import javax.crypto.SecretKey;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * Token validator based on the JJWT library.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class JjwtTokenValidator implements TokenValidator {

    /**
     * Token decoder. Without this the token claims can't be validated.
     */
    private final TokenDecoder tokenDecoder;

    /**
     * Constructs a validator with the received arguments.
     *
     * @param secretKey
     *            secret key used for the token
     */
    public JjwtTokenValidator(final SecretKey secretKey) {
        super();

        tokenDecoder = new JjwtTokenDecoder(secretKey);
    }

    @Override
    public final boolean hasExpired(final String token) {
        final LocalDateTime expiration;
        final LocalDateTime current;
        Boolean             expired;

        try {
            // Acquire expiration date claim
            expiration = tokenDecoder.decode(token)
                .getExpiration();

            if (expiration != null) {
                // Compare expiration to current date
                current = LocalDateTime.now();
                expired = expiration.isBefore(current);
                log.debug("Expired '{}' as token expires on {}, and the current date is {}.", expired, expiration,
                    current);
            } else {
                // No expiration
                expired = false;
                log.debug("The token has no expiration date");
            }

        } catch (final ExpiredJwtException e) {
            // Token parsing failed due to expiration date
            log.debug(e.getLocalizedMessage());
            expired = true;
        }

        return expired;
    }

}
