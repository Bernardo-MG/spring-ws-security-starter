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

package com.bernardomg.security.user.token.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.bernardomg.security.user.token.domain.exception.ConsumedTokenException;
import com.bernardomg.security.user.token.domain.exception.ExpiredTokenException;
import com.bernardomg.security.user.token.domain.exception.OutOfScopeTokenException;
import com.bernardomg.security.user.token.domain.exception.RevokedTokenException;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * Immutable implementation of the user token.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Builder(setterPrefix = "with")
@Slf4j
public record UserToken(String username, String name, String scope, String token, LocalDateTime creationDate,
        LocalDateTime expirationDate, Boolean consumed, Boolean revoked) {

    /**
     * Creates a copy of the token with the consumed flag active.
     *
     * @return a consumed copy of the token
     */
    public final UserToken consume() {
        return UserToken.builder()
            .withUsername(username)
            .withName(name)
            .withScope(scope)
            .withToken(token)
            .withCreationDate(creationDate)
            .withExpirationDate(expirationDate)
            .withConsumed(true)
            .withRevoked(revoked)
            .build();
    }

    public static final UserToken create(final String usrname, final String scpe, final Duration validity) {
        final String        tokenCode;
        final LocalDateTime creation;
        final LocalDateTime expiration;

        creation = LocalDateTime.now();
        expiration = creation.plus(validity);

        tokenCode = UUID.randomUUID()
            .toString();
        return UserToken.builder()
            .withUsername(usrname)
            .withScope(scpe)
            .withToken(tokenCode)
            .withCreationDate(creation)
            .withExpirationDate(expiration)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    /**
     * Creates a copy of the token with the revoked flag active.
     *
     * @return a revoked copy of the token
     */
    public final UserToken revoke() {
        return UserToken.builder()
            .withUsername(username)
            .withName(name)
            .withScope(scope)
            .withToken(token)
            .withCreationDate(creationDate)
            .withExpirationDate(expirationDate)
            .withConsumed(consumed)
            .withRevoked(true)
            .build();
    }

    public final void checkStatus(final String tokenScope) {
        if (!tokenScope.equals(scope)) {
            // Scope mismatch
            log.warn("Expected scope {}, but the token is for {}", tokenScope, scope);
            throw new OutOfScopeTokenException(token, tokenScope, scope);
        }
        if (consumed) {
            // Consumed
            // It isn't a valid token
            log.warn("Consumed token: {}", token);
            throw new ConsumedTokenException(token);
        }
        if (revoked) {
            // Revoked
            // It isn't a valid token
            log.warn("Revoked token: {}", token);
            throw new RevokedTokenException(token);
        }
        if (LocalDateTime.now()
            .isAfter(expirationDate)) {
            // Expired
            // It isn't a valid token
            log.warn("Expired token: {}", token);
            throw new ExpiredTokenException(token);
        }
    }

}
