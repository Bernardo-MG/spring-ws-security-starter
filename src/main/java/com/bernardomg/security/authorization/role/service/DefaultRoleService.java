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

package com.bernardomg.security.authorization.role.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.model.request.RoleUpdate;
import com.bernardomg.security.authorization.role.persistence.model.RoleEntity;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.validation.CreateRoleValidator;
import com.bernardomg.security.authorization.role.validation.DeleteRoleValidator;
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
    public final void delete(final long roleId) {
        final Optional<RoleEntity> readRole;

        log.debug("Deleting role {}", roleId);

        readRole = roleRepository.findById(roleId);

        if (readRole.isEmpty()) {
            throw new MissingRoleIdException(roleId);
        }

        validatorDeleteRole.validate(roleId);

        roleRepository.deleteById(roleId);
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
    public final Optional<Role> getOne(final long roleId) {

        log.debug("Reading role with id {}", roleId);

        // TODO: Use the read optional
        if (!roleRepository.existsById(roleId)) {
            throw new MissingRoleIdException(roleId);
        }

        return roleRepository.findById(roleId)
            .map(this::toDto);
    }

    @Override
    public final Role update(final long id, final RoleUpdate role) {
        final RoleEntity entity;
        final RoleEntity created;

        log.debug("Updating role with id {} using data {}", id, role);

        if (!roleRepository.existsById(id)) {
            throw new MissingRoleIdException(id);
        }

        entity = toEntity(role);
        entity.setId(id);

        created = roleRepository.save(entity);

        return toDto(created);
    }

    private final Role toDto(final RoleEntity role) {
        return Role.builder()
            .withId(role.getId())
            .withName(role.getName())
            .build();
    }

    private final RoleEntity toEntity(final RoleQuery role) {
        return RoleEntity.builder()
            .withName(role.getName())
            .build();
    }

    private final RoleEntity toEntity(final RoleUpdate role) {
        return RoleEntity.builder()
            .withId(role.getId())
            .withName(role.getName())
            .build();
    }

}
