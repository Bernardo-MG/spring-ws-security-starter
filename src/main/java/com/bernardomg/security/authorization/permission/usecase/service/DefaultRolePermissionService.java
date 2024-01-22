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

package com.bernardomg.security.authorization.permission.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionKey;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.exception.MissingResourcePermissionNameException;
import com.bernardomg.security.authorization.permission.domain.exception.MissingRolePermissionIdException;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;

import lombok.extern.slf4j.Slf4j;

/**
 * Default role permissions service.
 * <p>
 * <h2>Validations</h2> All relationships are validated, this means verifying that:
 * <ul>
 * <li>Permission exists</li>
 * <li>Role exists</li>
 * <li>Role permission exists, only when removing</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultRolePermissionService implements RolePermissionService {

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionRepository resourcePermissionRepository;

    /**
     * Role permissions repository. Used to modify permissions for the roles.
     */
    private final RolePermissionRepository     rolePermissionRepository;

    /**
     * Role repository. Used to validate the role exists.
     */
    private final RoleSpringRepository         roleRepository;

    public DefaultRolePermissionService(final RoleSpringRepository roleRepo,
            final ResourcePermissionRepository resourcePermissionRepo,
            final RolePermissionRepository rolePermissionRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo);
        rolePermissionRepository = Objects.requireNonNull(rolePermissionRepo);
    }

    @Override
    public final ResourcePermission addPermission(final String role, final String permission) {
        final RolePermissionEntity               rolePermission;
        final Optional<RoleEntity>               readRole;
        final Optional<ResourcePermissionEntity> readPermission;

        log.debug("Adding permission {} for role {}", permission, role);

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        readPermission = resourcePermissionRepository.findByName(permission);

        if (readPermission.isEmpty()) {
            throw new MissingResourcePermissionNameException(permission);
        }

        // Granted permission
        rolePermission = getRolePermissionSample(readRole.get()
            .getId(), permission);
        rolePermission.setGranted(true);

        // Persist relationship entities
        rolePermissionRepository.save(rolePermission);

        return toDto(readPermission.get());
    }

    @Override
    public final Iterable<ResourcePermission> getAvailablePermissions(final String role, final Pageable pageable) {
        final Optional<RoleEntity> readRole;

        log.debug("Reading available permissions for {}", role);

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        return resourcePermissionRepository.findAllAvailableToRole(readRole.get()
            .getId(), pageable)
            .map(this::toDto);
    }

    @Override
    public final Iterable<ResourcePermission> getPermissions(final String role, final Pageable page) {
        final Optional<RoleEntity> readRole;

        log.debug("Reading permissions for {}", role);

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        return resourcePermissionRepository.findAllForRole(readRole.get()
            .getId(), page)
            .map(this::toDto);
    }

    @Override
    public final ResourcePermission removePermission(final String role, final String permission) {
        final RolePermissionEntity               rolePermissionSample;
        final boolean                            rolePermissionExists;
        final Optional<ResourcePermissionEntity> readPermission;
        final Optional<RoleEntity>               readRole;
        final RolePermissionKey                  rolePermissionKey;

        log.debug("Removing permission {} for role {}", permission, role);

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        readPermission = resourcePermissionRepository.findByName(permission);

        if (readPermission.isEmpty()) {
            throw new MissingResourcePermissionNameException(permission);
        }

        rolePermissionExists = rolePermissionRepository.existsByRoleIdAndPermissionAndGranted(readRole.get()
            .getId(), permission, true);

        if (!rolePermissionExists) {
            rolePermissionKey = RolePermissionKey.builder()
                .withRoleId(readRole.get()
                    .getId())
                .withPermission(permission)
                .build();
            throw new MissingRolePermissionIdException(rolePermissionKey);
        }

        // Not granted permission
        rolePermissionSample = getRolePermissionSample(readRole.get()
            .getId(), permission);
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
