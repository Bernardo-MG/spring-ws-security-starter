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

package com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.domain.repository.RolePermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Role permission repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
@Transactional
public final class JpaRolePermissionRepository implements RolePermissionRepository {

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    private final RoleSpringRepository               roleSpringRepository;

    public JpaRolePermissionRepository(final RoleSpringRepository roleSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo) {
        super();

        roleSpringRepository = roleSpringRepo;
        resourcePermissionSpringRepository = resourcePermissionSpringRepo;
    }

    @Override
    public final Iterable<ResourcePermission> findAvailablePermissions(final String role, final Pageable pageable) {
        final Optional<RoleEntity>         readRole;
        final RoleEntity                   roleEntity;
        final Iterable<ResourcePermission> permissions;

        log.debug("Reading available permissions for {}", role);

        readRole = roleSpringRepository.findByName(role);

        if (readRole.isPresent()) {
            roleEntity = readRole.get();
            permissions = resourcePermissionSpringRepository.findAllAvailableToRole(roleEntity.getId(), pageable)
                .map(this::toDomain);
        } else {
            log.warn("Role {} doesn't exist. Can't find available permissions", role);
            permissions = List.of();
        }

        return permissions;
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

}
