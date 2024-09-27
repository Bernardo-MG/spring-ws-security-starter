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

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

/**
 * Immutable implementation of the user token.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Value
@Builder(setterPrefix = "with")
public final class UserToken {

    /**
     * Token consumed flag.
     */
    private final Boolean       consumed;

    /**
     * Token creation date.
     */
    private final LocalDateTime creationDate;

    /**
     * Token expiration date.
     */
    private final LocalDateTime expirationDate;

    /**
     * User name.
     */
    private final String        name;

    /**
     * Token revoked flag.
     */
    private final Boolean       revoked;

    /**
     * Token scope.
     */
    private final String        scope;

    /**
     * Token code.
     */
    private final String        token;

    /**
     * Token username.
     */
    private final String        username;

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

}
