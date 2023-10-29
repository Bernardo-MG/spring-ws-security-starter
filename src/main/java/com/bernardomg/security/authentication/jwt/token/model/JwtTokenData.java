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

package com.bernardomg.security.authentication.jwt.token.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents the common JWT token claims.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface JwtTokenData {

    /**
     * Returns the audience.
     *
     * @return the audience
     */
    public String getAudience();

    /**
     * Returns the expiration date.
     *
     * @return the expiration date
     */
    public LocalDateTime getExpiration();

    /**
     * Returns the id.
     *
     * @return the id
     */
    public String getId();

    /**
     * Returns the issued at date.
     *
     * @return the issued at date
     */
    public LocalDateTime getIssuedAt();

    /**
     * Returns the issuer.
     *
     * @return the issuer
     */
    public String getIssuer();

    /**
     * Returns the not before date.
     *
     * @return the not before date
     */
    public LocalDateTime getNotBefore();

    /**
     * Returns the permissions.
     *
     * @return the permissions
     */
    public Map<String, List<String>> getPermissions();

    /**
     * Returns the subject.
     *
     * @return the subject
     */
    public String getSubject();

}
