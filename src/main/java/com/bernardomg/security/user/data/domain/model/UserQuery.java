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

package com.bernardomg.security.user.data.domain.model;

import java.util.Objects;

import lombok.Builder;

/**
 * Data for querying users.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Builder(setterPrefix = "with")
public record UserQuery(String email, Boolean enabled, Boolean notExpired, Boolean locked, String name,
        Boolean passwordNotExpired, String username) {

    public UserQuery(final String email, final Boolean enabled, final Boolean notExpired, final Boolean locked,
            final String name, final Boolean passwordNotExpired, final String username) {
        if (Objects.nonNull(name)) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
        if (Objects.nonNull(username)) {
            this.username = username.trim()
                .toLowerCase();
        } else {
            this.username = null;
        }
        if (Objects.nonNull(email)) {
            this.email = email.trim()
                .toLowerCase();
        } else {
            this.email = null;
        }

        this.enabled = enabled;
        this.notExpired = notExpired;
        this.locked = locked;
        this.passwordNotExpired = passwordNotExpired;
    }

}
