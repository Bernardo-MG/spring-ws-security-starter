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

package com.bernardomg.security.initializer;

import java.util.Collection;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.constant.Actions;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Creates initial test roles on app start. These are meant to allow local development.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class TestRolesInitializer {

    private final ResourcePermissionRepository resourcePermissionRepository;

    private final RolePermissionRepository     rolePermissionRepository;

    private final RoleRepository               roleRepository;

    public TestRolesInitializer(final ResourcePermissionRepository permissionRepo, final RoleRepository roleRepo,
            final RolePermissionRepository rolePermissionRepo) {
        super();

        resourcePermissionRepository = permissionRepo;
        roleRepository = roleRepo;
        rolePermissionRepository = rolePermissionRepo;
    }

    public final void initialize() {
        final Collection<ResourcePermissionEntity> permissions;

        log.debug("Initializing test roles");

        permissions = resourcePermissionRepository.findAll();

        runIfNotExists(() -> initializeAdminRole(permissions), "ADMIN");
        runIfNotExists(() -> initializeReadRole(permissions), "READ");

        log.debug("Initialized test roles");
    }

    private final RoleEntity getReadRole() {
        return RoleEntity.builder()
            .withName("READ")
            .build();
    }

    private final RoleEntity getRootRole() {
        return RoleEntity.builder()
            .withName("ADMIN")
            .build();
    }

    private final void initializeAdminRole(final Collection<ResourcePermissionEntity> permissions) {
        final RoleEntity     rootRole;
        final RoleEntity     savedRootRole;
        RolePermissionEntity rolePermission;

        // Add read user
        rootRole = getRootRole();
        savedRootRole = roleRepository.save(rootRole);

        for (final ResourcePermissionEntity perm : permissions) {
            rolePermission = RolePermissionEntity.builder()
                .withRoleId(savedRootRole.getId())
                .withPermission(perm.getName())
                .withGranted(true)
                .build();
            rolePermissionRepository.save(rolePermission);
        }
    }

    private final void initializeReadRole(final Collection<ResourcePermissionEntity> permissions) {
        final RoleEntity readRole;
        final RoleEntity savedReadRole;

        // Add read user
        readRole = getReadRole();
        savedReadRole = roleRepository.save(readRole);

        setPermissions(savedReadRole, permissions, Actions.READ);
        setPermissions(savedReadRole, permissions, Actions.VIEW);
    }

    private final void runIfNotExists(final Runnable runnable, final String name) {
        if (!roleRepository.existsByName(name)) {
            runnable.run();
            log.debug("Initialized {} role", name);
        } else {
            log.debug("Role {} already exists. Skipped initialization.", name);
        }
    }

    private final void setPermissions(final RoleEntity role, final Collection<ResourcePermissionEntity> permissions,
            final String actionName) {
        RolePermissionEntity                       rolePermission;
        final Collection<ResourcePermissionEntity> validPermissions;

        validPermissions = permissions.stream()
            .filter(p -> p.getAction()
                .equals(actionName))
            .toList();
        for (final ResourcePermissionEntity permission : validPermissions) {
            rolePermission = RolePermissionEntity.builder()
                .withRoleId(role.getId())
                .withPermission(permission.getName())
                .withGranted(true)
                .build();
            rolePermissionRepository.save(rolePermission);
        }
    }

}
