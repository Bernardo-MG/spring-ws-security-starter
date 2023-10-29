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

package com.bernardomg.security.user.token.model;

import java.time.LocalDateTime;

/**
 * User token.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserToken {

    /**
     * Returns the date at which the token was created.
     *
     * @return the date at which the token was created
     */
    public LocalDateTime getCreationDate();

    /**
     * Returns the date at which the token will expire.
     *
     * @return the date at which the token will expire
     */
    public LocalDateTime getExpirationDate();

    /**
     * Returns the token id.
     *
     * @return the token id
     */
    public Long getId();

    /**
     * Returns the name of the user linked to the token.
     *
     * @return the name of the user linked to the token
     */
    public String getName();

    /**
     * Returns the token scope.
     *
     * @return the token scope
     */
    public String getScope();

    /**
     * Returns the token code.
     *
     * @return the token code
     */
    public String getToken();

    /**
     * Returns the username of the user linked to the token.
     *
     * @return the username of the user linked to the token
     */
    public String getUsername();

    /**
     * Indicates if the token is consumed
     *
     * @return {@code true} if the token is consumed, {@code false} otherwise
     */
    public boolean isConsumed();

    /**
     * Indicates if the token is revoked
     *
     * @return {@code true} if the token is revoked, {@code false} otherwise
     */
    public boolean isRevoked();

}
