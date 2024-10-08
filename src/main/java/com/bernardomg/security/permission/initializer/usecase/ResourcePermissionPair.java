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

package com.bernardomg.security.permission.initializer.usecase;

import lombok.Builder;
import lombok.Value;

/**
 * Resource and action pair for the permissions loader. This pair represents a permission which to be loaded.
 */
@Value
@Builder(setterPrefix = "with")
public final class ResourcePermissionPair {

    /**
     * Creates a resource permission pair with the received resource and action.
     *
     * @param resource
     *            permission resource
     * @param action
     *            permission action
     * @return resource permission pair for the received resource and action
     */
    public static final ResourcePermissionPair of(final String resource, final String action) {
        return ResourcePermissionPair.builder()
            .withAction(action)
            .withResource(resource)
            .build();
    }

    /**
     * Action name.
     */
    private final String action;

    /**
     * Resource name.
     */
    private final String resource;

}
