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

package com.bernardomg.security.authorization.permission.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.exception.MissingResourcePermissionIdException;
import com.bernardomg.security.authorization.permission.exception.MissingRolePermissionIdException;
import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionKey;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.persistence.model.RoleEntity;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Default role permissions service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultRolePermissionService implements RolePermissionService {

    /**
     * Resource permissions repository.
     */
    private final ResourcePermissionRepository resourcePermissionRepository;

    /**
     * Role permissions repository.
     */
    private final RolePermissionRepository     rolePermissionRepository;

    /**
     * Role repository.
     */
    private final RoleRepository               roleRepository;

    public DefaultRolePermissionService(final RoleRepository roleRepo,
            final ResourcePermissionRepository resourcePermissionRepo,
            final RolePermissionRepository rolePermissionRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo);
        rolePermissionRepository = Objects.requireNonNull(rolePermissionRepo);
    }

    @Override
    public final ResourcePermission addPermission(final long roleId, final String permission) {
        final RolePermissionEntity               rolePermission;
        final Optional<RoleEntity>               readRole;
        final Optional<ResourcePermissionEntity> readPermission;

        log.debug("Adding permission {} for role {}", permission, roleId);

        readRole = roleRepository.findById(roleId);

        if (readRole.isEmpty()) {
            throw new MissingRoleIdException(roleId);
        }

        readPermission = resourcePermissionRepository.findByName(permission);

        if (readPermission.isEmpty()) {
            throw new MissingResourcePermissionIdException(permission);
        }

        // Build relationship entities
        rolePermission = getRolePermissionSample(roleId, readPermission.get()
            .getName());
        rolePermission.setGranted(true);

        // Persist relationship entities
        rolePermissionRepository.save(rolePermission);

        return toDto(readPermission.get());
    }

    @Override
    public final Iterable<ResourcePermission> getAvailablePermissions(final long roleId, final Pageable pageable) {
        final Optional<RoleEntity> readRole;

        readRole = roleRepository.findById(roleId);

        if (readRole.isEmpty()) {
            throw new MissingRoleIdException(roleId);
        }

        return resourcePermissionRepository.findAllAvailableToRole(roleId, pageable)
            .map(this::toDto);
    }

    @Override
    public final Iterable<ResourcePermission> getPermissions(final long roleId, final Pageable page) {
        final Optional<RoleEntity> readRole;

        readRole = roleRepository.findById(roleId);

        if (readRole.isEmpty()) {
            throw new MissingRoleIdException(roleId);
        }

        return resourcePermissionRepository.findAllForRole(roleId, page)
            .map(this::toDto);
    }

    @Override
    public final ResourcePermission removePermission(final long roleId, final String permission) {
        final RolePermissionEntity               rolePermissionSample;
        final Optional<RolePermissionEntity>     readRolePermission;
        final Optional<ResourcePermissionEntity> readPermission;
        final Optional<RoleEntity>               readRole;
        final RolePermissionKey                  rolePermissionKey;

        log.debug("Removing permission {} for role {}", permission, roleId);

        readRole = roleRepository.findById(roleId);

        if (readRole.isEmpty()) {
            throw new MissingRoleIdException(roleId);
        }

        readPermission = resourcePermissionRepository.findByName(permission);

        if (readPermission.isEmpty()) {
            throw new MissingResourcePermissionIdException(permission);
        }

        rolePermissionKey = RolePermissionKey.builder()
            .withRoleId(roleId)
            .withPermission(readPermission.get()
                .getName())
            .build();
        readRolePermission = rolePermissionRepository.findById(rolePermissionKey);

        if (readRolePermission.isEmpty()) {
            throw new MissingRolePermissionIdException(rolePermissionKey);
        }

        // Build relationship entities
        rolePermissionSample = getRolePermissionSample(roleId, readPermission.get()
            .getName());
        rolePermissionSample.setGranted(false);

        // Delete relationship entities
        rolePermissionRepository.save(rolePermissionSample);

        return toDto(readPermission.get());
    }

    private final RolePermissionEntity getRolePermissionSample(final long roleId, final String permission) {
        return RolePermissionEntity.builder()
            .withRoleId(roleId)
            .withPermission(permission)
            .build();
    }

    private final ResourcePermission toDto(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

}
