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

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Representation of a user.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Value
@Builder(setterPrefix = "with")
public final class User {

    /**
     * User email.
     */
    @NonNull
    private final String  email;

    /**
     * User enabled flag.
     */
    private final boolean enabled;

    /**
     * User expired flag.
     */
    private final boolean expired;

    /**
     * User locked flag.
     */
    private final boolean locked;

    /**
     * User name.
     */
    @NonNull
    private final String  name;

    /**
     * Password expired flag.
     */
    private final boolean passwordExpired;

    /**
     * User username.
     */
    @NonNull
    private final String  username;

}
