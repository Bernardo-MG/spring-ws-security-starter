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

package com.bernardomg.security.authentication.user.model;

/**
 * User data.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface User {

    /**
     * Returns the user email.
     *
     * @return the user email
     */
    public String getEmail();

    /**
     * Returns the user id.
     *
     * @return the user id
     */
    public long getId();

    /**
     * Returns the user name.
     *
     * @return the user name
     */
    public String getName();

    /**
     * Returns the user username.
     *
     * @return the user username
     */
    public String getUsername();

    /**
     * Returns the user enabled flag.
     *
     * @return the user enabled flag
     */
    public boolean isEnabled();

    /**
     * Returns the user expired flag.
     * <p>
     * This means the user is no longer valid.
     *
     * @return the user expired flag
     */
    public boolean isExpired();

    /**
     * Returns the user locked flag.
     *
     * @return the user locked flag
     */
    public boolean isLocked();

    /**
     * Returns the password expired flag.
     *
     * @return the password expired flag
     */
    public boolean isPasswordExpired();

}
