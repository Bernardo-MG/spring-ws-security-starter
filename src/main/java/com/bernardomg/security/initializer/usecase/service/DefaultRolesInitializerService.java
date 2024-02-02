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

package com.bernardomg.security.initializer.usecase.service;

import java.util.Collection;

import com.bernardomg.security.authorization.permission.constant.Actions;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Creates initial test roles on app start. These are meant to help local development.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultRolesInitializerService implements RolesInitializerService {

    private final ResourcePermissionRepository resourcePermissionRepository;

    private final RolePermissionRepository     rolePermissionRepository;

    private final RoleRepository               roleRepository;

    public DefaultRolesInitializerService(final ResourcePermissionRepository permissionRepo,
            final RoleRepository roleRepo, final RolePermissionRepository rolePermissionRepo) {
        super();

        resourcePermissionRepository = permissionRepo;
        roleRepository = roleRepo;
        rolePermissionRepository = rolePermissionRepo;
    }

    @Override
    public final void initialize() {
        final Collection<ResourcePermission> permissions;

        log.debug("Initializing test roles");

        permissions = resourcePermissionRepository.findAll();

        runIfNotExists(() -> initializeAdminRole(permissions), "ADMIN");
        runIfNotExists(() -> initializeReadRole(permissions), "READ");

        log.debug("Initialized test roles");
    }

    private final Role getReadRole() {
        return Role.builder()
            .withName("READ")
            .build();
    }

    private final Role getRootRole() {
        return Role.builder()
            .withName("ADMIN")
            .build();
    }

    private final void initializeAdminRole(final Collection<ResourcePermission> permissions) {
        final Role     rootRole;
        final Role     savedRootRole;
        RolePermission rolePermission;

        // Add read user
        rootRole = getRootRole();
        savedRootRole = roleRepository.save(rootRole);

        for (final ResourcePermission permission : permissions) {
            rolePermission = RolePermission.builder()
                .withRole(savedRootRole.getName())
                .withPermission(permission.getName())
                .build();
            rolePermissionRepository.save(rolePermission);
        }
    }

    private final void initializeReadRole(final Collection<ResourcePermission> permissions) {
        final Role readRole;
        final Role savedReadRole;

        // Add read user
        readRole = getReadRole();
        savedReadRole = roleRepository.save(readRole);

        setPermissions(savedReadRole, permissions, Actions.READ);
        setPermissions(savedReadRole, permissions, Actions.VIEW);
    }

    private final void runIfNotExists(final Runnable runnable, final String name) {
        if (!roleRepository.exists(name)) {
            runnable.run();
            log.debug("Initialized {} role", name);
        } else {
            log.debug("Role {} already exists. Skipped initialization.", name);
        }
    }

    private final void setPermissions(final Role role, final Collection<ResourcePermission> permissions,
            final String actionName) {
        final Collection<ResourcePermission> validPermissions;
        RolePermission                       rolePermission;

        validPermissions = permissions.stream()
            .filter(p -> p.getAction()
                .equals(actionName))
            .toList();
        for (final ResourcePermission permission : validPermissions) {
            rolePermission = RolePermission.builder()
                .withRole(role.getName())
                .withPermission(permission.getName())
                .build();
            rolePermissionRepository.save(rolePermission);
        }
    }

}
