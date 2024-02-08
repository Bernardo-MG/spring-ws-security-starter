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

package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Role permission repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
public final class JpaRolePermissionRepository implements RolePermissionRepository {

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    private final RolePermissionSpringRepository     rolePermissionSpringRepository;

    private final RoleSpringRepository               roleSpringRepository;

    public JpaRolePermissionRepository(final RoleSpringRepository roleRepo,
            final RolePermissionSpringRepository rolePermissionRepo,
            final ResourcePermissionSpringRepository resourcePermissionRepo) {
        super();

        roleSpringRepository = roleRepo;
        rolePermissionSpringRepository = rolePermissionRepo;
        resourcePermissionSpringRepository = resourcePermissionRepo;
    }

    @Override
    public final RolePermission delete(final RolePermission permission) {
        final RolePermissionEntity toSave;
        final Optional<RoleEntity> readRole;
        final RolePermission       removed;

        log.debug("Removing permission {} for role {}", permission.getPermission(), permission.getRole());

        readRole = roleSpringRepository.findOneByName(permission.getRole());

        if (readRole.isPresent()) {
            // Not granted permission
            toSave = getRolePermissionSample(readRole.get()
                .getId(), permission.getPermission());
            toSave.setGranted(false);

            // The permissions are not deleted
            // Instead they are set to not granted
            rolePermissionSpringRepository.save(toSave);

            removed = toDomain(toSave, readRole.get());
        } else {
            log.warn("Role {} doesn't exist. Can't remove permission {}", permission.getRole(),
                permission.getPermission());
            removed = null;
        }

        return removed;
    }

    @Override
    public final boolean exists(final String role, final String permission) {
        final Optional<RoleEntity> readRole;
        final long                 id;
        final boolean              exists;

        log.debug("Checking if permission {} exists for role {}", permission, role);

        readRole = roleSpringRepository.findOneByName(role);
        if (readRole.isPresent()) {
            id = readRole.get()
                .getId();
            exists = rolePermissionSpringRepository.existsByRoleIdAndPermissionAndGranted(id, permission, true);
        } else {
            log.warn("Role {} doesn't exist. Can't check for permission {} existence", role, permission);
            exists = false;
        }

        return exists;
    }

    @Override
    public final Iterable<ResourcePermission> findAvailablePermissions(final String role, final Pageable pageable) {
        final Optional<RoleEntity>         readRole;
        final RoleEntity                   roleEntity;
        final Iterable<ResourcePermission> permissions;

        log.debug("Reading available permissions for {}", role);

        readRole = roleSpringRepository.findOneByName(role);

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

    @Override
    public final Iterable<ResourcePermission> findPermissions(final String role, final Pageable page) {
        final Optional<RoleEntity>         readRole;
        final Iterable<ResourcePermission> permissions;

        log.debug("Reading permissions for {}", role);

        readRole = roleSpringRepository.findOneByName(role);
        if (readRole.isPresent()) {
            permissions = resourcePermissionSpringRepository.findAllForRole(readRole.get()
                .getId(), page)
                .map(this::toDomain);
        } else {
            log.warn("Role {} doesn't exist. Can't find its permissions", role);
            permissions = List.of();
        }

        return permissions;
    }

    @Override
    public final RolePermission save(final RolePermission permission) {
        final RolePermissionEntity toSave;
        final Optional<RoleEntity> role;
        final RolePermissionEntity saved;
        final RolePermission       created;

        log.debug("Adding permission {} to role {}", permission.getPermission(), permission.getRole());

        role = roleSpringRepository.findOneByName(permission.getRole());
        if (role.isPresent()) {
            toSave = RolePermissionEntity.builder()
                .withRoleId(role.get()
                    .getId())
                .withPermission(permission.getPermission())
                .withGranted(true)
                .build();
            saved = rolePermissionSpringRepository.save(toSave);

            created = toDomain(saved, role.get());
        } else {
            log.warn("Role {} doesn't exist. Can't add permission {}", permission.getRole(),
                permission.getPermission());
            created = RolePermission.builder()
                .build();
        }

        return created;
    }

    private final RolePermissionEntity getRolePermissionSample(final long roleId, final String permission) {
        return RolePermissionEntity.builder()
            .withRoleId(roleId)
            .withPermission(permission)
            .build();
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

    private final RolePermission toDomain(final RolePermissionEntity permission, final RoleEntity role) {
        return RolePermission.builder()
            .withPermission(permission.getPermission())
            .withRole(role.getName())
            .build();
    }

}
