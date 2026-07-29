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

package com.bernardomg.security.adapter.inbound.jpa.repository.role;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.bernardomg.security.adapter.inbound.jpa.model.audit.AuditMetadata;
import com.bernardomg.security.adapter.inbound.jpa.model.role.RoleEntity;
import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.ResourcePermissionEntityMapper;
import com.bernardomg.security.domain.audit.model.AuditDetails;
import com.bernardomg.security.domain.audit.model.AuditDetails.AuditUser;
import com.bernardomg.security.domain.permission.comparator.ResourcePermissionComparator;
import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.role.filter.RoleFilter;
import com.bernardomg.security.domain.role.model.Role;

/**
 * Role repository mapper.
 */
public final class RoleEntityMapper {

    public static final Role toDomain(final RoleEntity role) {
        final Collection<ResourcePermission> permissions;
        final AuditDetails                   audit;

        if (role.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = role.getPermissions()
                .stream()
                .filter(Objects::nonNull)
                .map(ResourcePermissionEntityMapper::toDomain)
                // TODO: should sort in the query
                .sorted(new ResourcePermissionComparator())
                .toList();
        }

        audit = toDomain(role.getAudit());

        return new Role(role.getName(), permissions, audit);
    }

    public static final RoleEntity toEntity(final RoleFilter role) {
        final RoleEntity entity;

        // TODO: does it make sense filtering by name?
        entity = new RoleEntity();
        entity.setName(role.name()
            .orElse(null));

        return entity;
    }

    private static final AuditUser toAuditDomain(final UserEntity user) {
        final AuditUser auditUser;

        if (user == null) {
            auditUser = null;
        } else {
            auditUser = new AuditUser(user.getEmail(), user.getUsername(), user.getName());
        }

        return auditUser;
    }

    private static final AuditDetails toDomain(final AuditMetadata audit) {
        return new AuditDetails(audit.getCreatedAt(), toAuditDomain(audit.getCreatedBy()), audit.getUpdatedAt(),
            toAuditDomain(audit.getUpdatedBy()));
    }

    private RoleEntityMapper() {
        super();
    }

}
