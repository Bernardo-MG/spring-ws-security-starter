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

package com.bernardomg.security.role.adapter.inbound.jpa.model;

import java.util.Collection;
import java.util.Objects;

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourcePermissionEntityMapper;
import com.bernardomg.security.permission.domain.comparator.ResourcePermissionComparator;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;

/**
 * Role repository mapper.
 */
public final class RoleEntityMapper {

    public static final Role toDomain(final RoleEntity role) {
        final Collection<ResourcePermission> permissions;

        permissions = role.getPermissions()
            .stream()
            .filter(Objects::nonNull)
            .filter(RolePermissionEntity::getGranted)
            .map(RolePermissionEntity::getResourcePermission)
            .map(ResourcePermissionEntityMapper::toDomain)
            .sorted(new ResourcePermissionComparator())
            .toList();
        return new Role(role.getName(), permissions);
    }

    public static final RoleEntity toEntity(final RoleQuery role) {
        final RoleEntity entity;

        // TODO: does it make sense filtering by name?
        entity = new RoleEntity();
        entity.setName(role.name());

        return entity;
    }

    private RoleEntityMapper() {
        super();
    }

}
