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

package com.bernardomg.security.user.permission.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.domain.comparator.ResourcePermissionComparator;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.permission.domain.repository.UserRoleRepository;

/**
 * Role repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaUserRoleRepository implements UserRoleRepository {

    /**
     * Role repository.
     */
    private final RoleSpringRepository roleSpringRepository;

    public JpaUserRoleRepository(final RoleSpringRepository roleSpringRepo) {
        super();

        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
    }

    @Override
    public final Iterable<Role> findAvailableToUser(final String username, final Pageable page) {
        // TODO: this doesn't need the full role model, just the names
        return roleSpringRepository.findAllByUser(username, page)
            .map(this::toDomain);
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return new ResourcePermission(entity.getResource(), entity.getAction());
    }

    private final Role toDomain(final RoleEntity role) {
        final Collection<ResourcePermission> permissions;

        if (role.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = role.getPermissions()
                .stream()
                .filter(Objects::nonNull)
                .filter(RolePermissionEntity::getGranted)
                .map(RolePermissionEntity::getResourcePermission)
                .map(this::toDomain)
                .sorted(new ResourcePermissionComparator())
                .toList();
        }
        return new Role(role.getName(), permissions);
    }

}
