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

package com.bernardomg.security.login.domain.model;

import lombok.Builder;
import lombok.Value;

/**
 * Immutable implementation of {@link TokenLoginStatus}.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Value
@Builder(setterPrefix = "with")
public final class TokenLoginStatus {

    /**
     * Creates a {@code TokenLoginStatus} with the logged in flag.
     *
     * @param lgd
     *            logged in flag
     * @return a {@code TokenLoginStatus}
     */
    public static final TokenLoginStatus of(final boolean lgd) {
        return TokenLoginStatus.builder()
            .withLogged(lgd)
            .withToken("")
            .build();
    }

    /**
     * Creates a {@code TokenLoginStatus} with the logged in flag and token.
     *
     * @param lgd
     *            logged in flag
     * @param tkn
     *            user token
     * @return a {@code TokenLoginStatus}
     */
    public static final TokenLoginStatus of(final boolean lgd, final String tkn) {
        return TokenLoginStatus.builder()
            .withLogged(lgd)
            .withToken(tkn)
            .build();
    }

    /**
     * Flag telling if the login was successful.
     */
    private final boolean logged;

    /**
     * Security token.
     */
    private final String  token;

}
