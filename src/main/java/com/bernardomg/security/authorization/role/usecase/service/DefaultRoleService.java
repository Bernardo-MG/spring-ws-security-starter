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

package com.bernardomg.security.authorization.role.usecase.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.domain.exception.MissingRoleException;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.usecase.validation.CreateRoleValidator;
import com.bernardomg.security.authorization.role.usecase.validation.DeleteRoleValidator;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Default role service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultRoleService implements RoleService {

    private final RoleRepository    roleRepository;

    private final Validator<Role>   validatorCreateRole;

    private final Validator<String> validatorDeleteRole;

    public DefaultRoleService(final RoleRepository roleRepo, final UserRoleRepository userRoleRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);

        validatorCreateRole = new CreateRoleValidator(roleRepo);
        validatorDeleteRole = new DeleteRoleValidator(userRoleRepo);
    }

    @Override
    public final Role create(final String name) {
        final Role entity;

        log.debug("Creating role {}", name);

        entity = Role.builder()
            .withName(name)
            .withPermissions(List.of())
            .build();

        validatorCreateRole.validate(entity);

        return roleRepository.save(entity);
    }

    @Override
    public final void delete(final String role) {
        final boolean exists;

        log.debug("Deleting role {}", role);

        exists = roleRepository.exists(role);
        if (!exists) {
            throw new MissingRoleException(role);
        }

        validatorDeleteRole.validate(role);

        roleRepository.delete(role);
    }

    @Override
    public final Iterable<Role> getAll(final RoleQuery sample, final Pageable pageable) {
        log.debug("Reading roles with sample {} and pagination {}", sample, pageable);

        return roleRepository.findAll(sample, pageable);
    }

    @Override
    public final Optional<Role> getOne(final String role) {
        final boolean exists;

        log.debug("Reading role {}", role);

        exists = roleRepository.exists(role);
        if (!exists) {
            throw new MissingRoleException(role);
        }

        return roleRepository.findOne(role);
    }

    @Override
    public final Role update(final Role role) {
        final Role    roleData;
        final boolean exists;

        log.debug("Updating role {} using data {}", role.getName(), role);

        exists = roleRepository.exists(role.getName());
        if (!exists) {
            throw new MissingRoleException(role.getName());
        }

        roleData = Role.builder()
            .withName(role.getName())
            .withPermissions(role.getPermissions())
            .build();

        return roleRepository.save(roleData);
    }

}
