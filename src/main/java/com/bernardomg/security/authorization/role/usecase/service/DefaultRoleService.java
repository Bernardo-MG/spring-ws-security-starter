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

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleChange;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;
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

    private final RoleRepository        roleRepository;

    private final Validator<RoleEntity> validatorCreateRole;

    private final Validator<Long>       validatorDeleteRole;

    public DefaultRoleService(final RoleRepository roleRepo, final UserRoleRepository userRoleRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);

        validatorCreateRole = new CreateRoleValidator(roleRepo);
        validatorDeleteRole = new DeleteRoleValidator(userRoleRepo);
    }

    @Override
    public final Role create(final String name) {
        final RoleEntity entity;
        final RoleEntity created;

        log.debug("Creating role {}", name);

        entity = RoleEntity.builder()
            .withName(name)
            .build();

        validatorCreateRole.validate(entity);

        created = roleRepository.save(entity);

        return toDto(created);
    }

    @Override
    public final void delete(final String role) {
        final Optional<RoleEntity> readRole;

        log.debug("Deleting role {}", role);

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        validatorDeleteRole.validate(readRole.get()
            .getId());

        roleRepository.deleteById(readRole.get()
            .getId());
    }

    @Override
    public final Iterable<Role> getAll(final RoleQuery sample, final Pageable pageable) {
        final RoleEntity entitySample;

        log.debug("Reading roles with sample {} and pagination {}", sample, pageable);

        entitySample = toEntity(sample);

        return roleRepository.findAll(Example.of(entitySample), pageable)
            .map(this::toDto);
    }

    @Override
    public final Optional<Role> getOne(final String role) {
        final Optional<RoleEntity> readRole;

        log.debug("Reading role {}", role);

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        return roleRepository.findById(readRole.get()
            .getId())
            .map(this::toDto);
    }

    @Override
    public final Role update(final String role, final RoleChange data) {
        final RoleEntity           entity;
        final RoleEntity           created;
        final Optional<RoleEntity> readRole;

        log.debug("Updating role {} using data {}", role, data);

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        entity = toEntity(data, readRole.get()
            .getId());

        created = roleRepository.save(entity);

        return toDto(created);
    }

    private final Role toDto(final RoleEntity role) {
        return Role.builder()
            .withName(role.getName())
            .build();
    }

    private final RoleEntity toEntity(final RoleChange role, final Long id) {
        return RoleEntity.builder()
            .withId(id)
            .withName(role.getName())
            .build();
    }

    private final RoleEntity toEntity(final RoleQuery role) {
        return RoleEntity.builder()
            .withName(role.getName())
            .build();
    }

}
