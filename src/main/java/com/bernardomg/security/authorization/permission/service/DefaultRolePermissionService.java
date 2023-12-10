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

import com.bernardomg.security.authorization.permission.exception.MissingRolePermissionIdException;
import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionKey;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.validation.AddRolePermissionValidator;
import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.authorization.role.persistence.model.RoleEntity;
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

    private final RoleRepository               roleRepository;

    private final Validator<RolePermission>    validatorAddRolePermission;

    public DefaultRolePermissionService(final RoleRepository roleRepo,
            final ResourcePermissionRepository permissionRepo, final RolePermissionRepository rolePermissionRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);
        permissionRepository = Objects.requireNonNull(permissionRepo);
        rolePermissionRepository = Objects.requireNonNull(rolePermissionRepo);

        validatorAddRolePermission = new AddRolePermissionValidator(permissionRepo);
    }

    @Override
    public final RolePermission addPermission(final long roleId, final long permission) {
        final RolePermissionEntity rolePermissionSample;
        final RolePermission       rolePermission;
        final RolePermissionEntity created;
        final Optional<RoleEntity> readRole;
        final RoleEntity           role;

        log.debug("Adding permission {} for role {}", permission, roleId);

        readRole = roleRepository.findById(roleId);

        if (readRole.isEmpty()) {
            throw new MissingRoleIdException(roleId);
        }

        role = readRole.get();
        rolePermission = RolePermission.builder()
            .withRoleId(role.getId())
            .withPermissionId(permission)
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
    public final RolePermission removePermission(final long roleId, final long permissionId) {
        final RolePermissionEntity           rolePermissionSample;
        final RolePermissionEntity           updated;
        final Optional<RolePermissionEntity> readPermission;
        final Optional<RoleEntity>           readRole;
        final RolePermissionKey              rolePermissionKey;

        log.debug("Removing permission {} for role {}", permissionId, roleId);

        readRole = roleRepository.findById(roleId);

        if (readRole.isEmpty()) {
            throw new MissingRoleIdException(roleId);
        }

        rolePermissionKey = RolePermissionKey.builder()
            .withRoleId(roleId)
            .withPermissionId(permissionId)
            .build();
        readPermission = rolePermissionRepository.findById(rolePermissionKey);

        if (readPermission.isEmpty()) {
            throw new MissingRolePermissionIdException(permissionId);
        }

        // Build relationship entities
        rolePermissionSample = getRolePermissionSample(roleId, permissionId);
        rolePermissionSample.setGranted(false);

        // Delete relationship entities
        updated = rolePermissionRepository.save(rolePermissionSample);

        return toDto(updated);
    }

    private final RolePermissionEntity getRolePermissionSample(final long roleId, final long permissionId) {
        return RolePermissionEntity.builder()
            .withRoleId(roleId)
            .withPermissionId(permissionId)
            .build();
    }

    private final ResourcePermission toDto(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withId(entity.getId())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

    private final RolePermission toDto(final RolePermissionEntity entity) {
        return RolePermission.builder()
            .withPermissionId(entity.getPermissionId())
            .withRoleId(entity.getRoleId())
            .build();
    }

}
