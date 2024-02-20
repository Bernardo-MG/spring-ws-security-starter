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

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleException;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Default role permissions service.
 * <h2>Validations</h2>
 * <p>
 * All relationships are validated, this means verifying that:
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
    public final Iterable<ResourcePermission> getAvailablePermissions(final String role, final Pageable pageable) {
        final boolean roleExists;

        log.debug("Reading available permissions for {}", role);

        roleExists = roleRepository.exists(role);
        if (!roleExists) {
            throw new MissingRoleException(role);
        }

        return rolePermissionRepository.findAvailablePermissions(role, pageable);
    }

}
