/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
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

package com.bernardomg.security.jwt.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * Immutable implementation of the JWT token data.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Value
@Builder(setterPrefix = "with")
@Slf4j
public final class JwtTokenData {

    /**
     * Audience.
     */
    private final Collection<String>        audience;

    /**
     * Expiration date.
     */
    private final LocalDateTime             expiration;

    /**
     * Id.
     */
    private final String                    id;

    /**
     * Issued at date.
     */
    private final LocalDateTime             issuedAt;

    /**
     * Issuer.
     */
    private final String                    issuer;

    /**
     * Not before date.
     */
    private final LocalDateTime             notBefore;

    /**
     * Permissions.
     */
    @Builder.Default
    private final Map<String, List<String>> permissions = Map.of();

    /**
     * Subject.
     */
    private final String                    subject;

    public final boolean isExpired() {
        final LocalDateTime current;
        final boolean       expired;

        // TODO: test this
        if (expiration != null) {
            // Compare expiration to current date
            current = LocalDateTime.now();
            expired = expiration.isBefore(current);
            log.debug("Expired '{}' as token expires on {}, and the current date is {}.", expired, expiration, current);
        } else {
            // No expiration
            expired = false;
            log.debug("The token has no expiration date");
        }

        return expired;
    }

}
