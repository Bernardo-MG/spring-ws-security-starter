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

package com.bernardomg.security.user.usecase.store;

import com.bernardomg.security.user.domain.exception.InvalidTokenException;

/**
 * Store for tokens linked to users.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserTokenStore {

    /**
     * Consumes the token, marking it as already used.
     *
     * @param token
     *            token to consume
     */
    public void consumeToken(final String token);

    /**
     * Returns a new token for a user.
     *
     * @param username
     *            username for the user who generates the token
     * @return token for the subject
     */
    public String createToken(final String username);

    /**
     * Returns the username for the token.
     * <p>
     * TODO: maybe return an optional
     *
     * @param token
     *            token to decode as the object
     * @return username for the token
     */
    public String getUsername(final String token);

    /**
     * Revokes all the tokens for a user, so they can no longer be used.
     *
     * @param username
     *            username for the user to revoke tokens for
     */
    public void revokeExistingTokens(final String username);

    /**
     *
     * Check if the token is valid, throwing an exception otherwise. This exception is expected to be a concrete type,
     * indicating the actual error.
     *
     * @param token
     *            token to validate
     * @throws InvalidTokenException
     *             if the token is invalid
     */
    public void validate(final String token) throws InvalidTokenException;

}
