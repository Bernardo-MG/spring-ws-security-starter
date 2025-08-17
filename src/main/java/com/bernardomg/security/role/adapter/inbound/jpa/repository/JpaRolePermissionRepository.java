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

package com.bernardomg.security.role.adapter.inbound.jpa.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.springframework.SpringPagination;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;

/**
 * Role permission repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaRolePermissionRepository implements RolePermissionRepository {

    /**
     * Logger for the class.
     */
    private static final Logger                      log = LoggerFactory.getLogger(JpaRolePermissionRepository.class);

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    /**
     * Role repository.
     */
    private final RoleSpringRepository               roleSpringRepository;

    public JpaRolePermissionRepository(final RoleSpringRepository roleSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo) {
        super();

        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
        resourcePermissionSpringRepository = Objects.requireNonNull(resourcePermissionSpringRepo);
    }

    @Override
    public final Page<ResourcePermission> findAvailablePermissions(final String role, final Pagination pagination,
            final Sorting sorting) {
        final Optional<RoleEntity>                                     readRole;
        final RoleEntity                                               roleEntity;
        final org.springframework.data.domain.Page<ResourcePermission> permissionsPage;
        final Page<ResourcePermission>                                 permissions;
        final Pageable                                                 pageable;

        log.debug("Reading available permissions for {}", role);

        readRole = roleSpringRepository.findByName(role);

        if (readRole.isPresent()) {
            roleEntity = readRole.get();
            pageable = SpringPagination.toPageable(pagination, sorting);
            permissionsPage = resourcePermissionSpringRepository.findAllAvailableToRole(roleEntity.getId(), pageable)
                .map(this::toDomain);
            permissions = new Page<>(permissionsPage.getContent(), permissionsPage.getSize(),
                permissionsPage.getNumber(), permissionsPage.getTotalElements(), permissionsPage.getTotalPages(),
                permissionsPage.getNumberOfElements(), permissionsPage.isFirst(), permissionsPage.isLast(), sorting);
        } else {
            log.warn("Role {} doesn't exist. Can't find available permissions", role);
            permissions = new Page<>(List.of(), 0, 0, 0, 0, 0, false, false, sorting);
        }

        return permissions;
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return new ResourcePermission(entity.getResource(), entity.getAction());
    }

}
