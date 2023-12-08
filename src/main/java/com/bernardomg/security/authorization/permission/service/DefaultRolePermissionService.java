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

import com.bernardomg.security.authorization.permission.model.ImmutableResourcePermission;
import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.validation.AddRolePermissionValidator;
import com.bernardomg.security.authorization.permission.validation.RemoveRolePermissionValidator;
import com.bernardomg.security.authorization.role.model.ImmutableRolePermission;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Default role permissions service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultRolePermissionService implements RolePermissionService {

    private final ResourcePermissionRepository permissionRepository;

    private final RolePermissionRepository     rolePermissionRepository;

    private final Validator<RolePermission>    validatorAddRolePermission;

    private final Validator<RolePermission>    validatorRemoveRolePermission;

    public DefaultRolePermissionService(final RoleRepository roleRepo,
            final ResourcePermissionRepository permissionRepo, final RolePermissionRepository rolePermissionRepo) {
        super();

        permissionRepository = Objects.requireNonNull(permissionRepo);
        rolePermissionRepository = Objects.requireNonNull(rolePermissionRepo);

        validatorAddRolePermission = new AddRolePermissionValidator(roleRepo, permissionRepo);
        validatorRemoveRolePermission = new RemoveRolePermissionValidator(rolePermissionRepo);
    }

    @Override
    public final RolePermission addPermission(final long roleId, final long permission) {
        final RolePermissionEntity rolePermissionSample;
        final RolePermission       rolePermission;
        final RolePermissionEntity created;

        log.debug("Adding permission {} for role {}", permission, roleId);

        rolePermission = ImmutableRolePermission.builder()
            .roleId(roleId)
            .permissionId(permission)
            .build();
        validatorAddRolePermission.validate(rolePermission);

        // Build relationship entities
        rolePermissionSample = getRolePermissionSample(roleId, permission);
        rolePermissionSample.setGranted(true);

        // Persist relationship entities
        created = rolePermissionRepository.save(rolePermissionSample);

        return toDto(created);
    }

    @Override
    public final Iterable<ResourcePermission> getAvailablePermissions(final long roleId, final Pageable pageable) {
        return permissionRepository.findAllAvailableToRole(roleId, pageable)
            .map(this::toDto);
    }

    @Override
    public final Iterable<ResourcePermission> getPermissions(final long roleId, final Pageable pageable) {
        return permissionRepository.findAllForRole(roleId, pageable)
            .map(this::toDto);
    }

    @Override
    public final RolePermission removePermission(final long roleId, final long permission) {
        final RolePermissionEntity               rolePermissionSample;
        final RolePermission                     rolePermission;
        final RolePermissionEntity               updated;
        final Optional<ResourcePermissionEntity> read;
        final RolePermission                     result;

        log.debug("Removing permission {} for role {}", permission, roleId);

        rolePermission = ImmutableRolePermission.builder()
            .roleId(roleId)
            .permissionId(permission)
            .build();
        validatorRemoveRolePermission.validate(rolePermission);

        read = permissionRepository.findById(permission);

        if (read.isPresent()) {
            // Build relationship entities
            rolePermissionSample = getRolePermissionSample(roleId, read.get()
                .getId());
            rolePermissionSample.setGranted(false);

            // Delete relationship entities
            updated = rolePermissionRepository.save(rolePermissionSample);

            result = toDto(updated);
        } else {
            result = ImmutableRolePermission.builder()
                .build();
        }

        return result;
    }

    private final RolePermissionEntity getRolePermissionSample(final long roleId, final long permissionId) {
        return RolePermissionEntity.builder()
            .roleId(roleId)
            .permissionId(permissionId)
            .build();
    }

    private final ResourcePermission toDto(final ResourcePermissionEntity entity) {
        return ImmutableResourcePermission.builder()
            .id(entity.getId())
            .resource(entity.getResource())
            .action(entity.getAction())
            .build();
    }

    private final RolePermission toDto(final RolePermissionEntity entity) {
        return ImmutableRolePermission.builder()
            .permissionId(entity.getPermissionId())
            .roleId(entity.getRoleId())
            .build();
    }

}
