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

package com.bernardomg.security.permission.adapter.inbound.jpa.model;

import com.bernardomg.security.permission.domain.model.ResourcePermission;

/**
 * Resource permission repository mapper.
 */
public final class ResourcePermissionEntityMapper {

    public static final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return new ResourcePermission(entity.getResource(), entity.getAction());
    }

    public static final ResourcePermissionEntity toEntity(final ResourcePermission permission) {
        final ResourcePermissionEntity entity;

        entity = new ResourcePermissionEntity();
        entity.setName(permission.getName());
        entity.setResource(permission.resource());
        entity.setAction(permission.action());

        return entity;
    }

}
