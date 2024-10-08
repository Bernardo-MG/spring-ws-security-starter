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

package com.bernardomg.security.web.whitelist;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpMethod;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Route to whitelist.
 */
@Value
@Builder(setterPrefix = "with")
@EqualsAndHashCode
public final class WhitelistRoute {

    public static WhitelistRoute of(final String route, final HttpMethod... methods) {
        for (final HttpMethod method : methods) {
            Objects.requireNonNull(method);
        }

        return WhitelistRoute.builder()
            .withRoute(route)
            .withMethods(Arrays.asList(methods))
            .build();
    }

    /**
     * Methods to whitelist.
     */
    @Builder.Default
    private final Collection<HttpMethod> methods = List.of();

    /**
     * Route to whitelist.
     */
    private final String                 route;

}
