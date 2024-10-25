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

package com.bernardomg.security.role.usecase.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.permission.data.domain.exception.MissingResourcePermissionException;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.role.domain.exception.MissingRoleException;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.usecase.validation.RoleHasNoUserRule;
import com.bernardomg.security.role.usecase.validation.RoleNameNotExistsRule;
import com.bernardomg.security.role.usecase.validation.RolePermissionsNotDuplicatedRule;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Default role service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
@Transactional
public final class DefaultRoleService implements RoleService {

    /**
     * Resource permission repository.
     */
    private final ResourcePermissionRepository resourcePermissionRepository;

    /**
     * Role repository.
     */
    private final RoleRepository               roleRepository;

    /**
     * Create validator.
     */
    private final Validator<Role>              validatorCreate;

    /**
     * Delete validator.
     */
    private final Validator<Role>              validatorDelete;

    /**
     * Update validator.
     */
    private final Validator<Role>              validatorUpdate;

    public DefaultRoleService(final RoleRepository roleRepo,
            final ResourcePermissionRepository resourcePermissionRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo);

        validatorCreate = new FieldRuleValidator<>(new RoleNameNotExistsRule(roleRepo));
        validatorDelete = new FieldRuleValidator<>(new RoleHasNoUserRule(roleRepo));
        validatorUpdate = new FieldRuleValidator<>(new RolePermissionsNotDuplicatedRule());
    }

    @Override
    public final Role create(final String name) {
        final Role role;

        log.debug("Creating role {}", name);

        role = new Role(name, List.of());

        validatorCreate.validate(role);

        return roleRepository.save(role);
    }

    @Override
    public final void delete(final String role) {
        final Role domainRole;

        log.debug("Deleting role {}", role);

        if (!roleRepository.exists(role)) {
            log.error("Missing role {}", role);
            throw new MissingRoleException(role);
        }

        domainRole = new Role(role, List.of());
        validatorDelete.validate(domainRole);

        roleRepository.delete(role);
    }

    @Override
    public final Iterable<Role> getAll(final RoleQuery sample, final Pageable pageable) {
        log.debug("Reading roles with sample {} and pagination {}", sample, pageable);

        return roleRepository.findAll(sample, pageable);
    }

    @Override
    public final Optional<Role> getOne(final String role) {
        final Optional<Role> read;

        log.debug("Reading role {}", role);

        read = roleRepository.findOne(role);
        if (read.isEmpty()) {
            log.error("Missing role {}", role);
            throw new MissingRoleException(role);
        }

        return read;
    }

    @Override
    public final Role update(final Role role) {
        log.debug("Updating role {} using data {}", role.name(), role);

        // Verify the role exists
        if (!roleRepository.exists(role.name())) {
            log.error("Missing role {}", role.name());
            throw new MissingRoleException(role.name());
        }

        // Verify the permissions exists
        for (final ResourcePermission permission : role.permissions()) {
            if (!resourcePermissionRepository.exists(permission.getName())) {
                // TODO: send all missing in a single exception
                throw new MissingResourcePermissionException(role.name());
            }
        }

        // Verify the permissions exist

        validatorUpdate.validate(role);

        return roleRepository.save(role);
    }

}
