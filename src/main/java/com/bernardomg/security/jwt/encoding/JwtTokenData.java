/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.jwt.encoding;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Immutable implementation of the JWT token data.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public record JwtTokenData(String id, String subject, String issuer, LocalDateTime issuedAt, LocalDateTime notBefore,
        LocalDateTime expiration, Collection<String> audience, Map<String, List<String>> permissions) {

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
