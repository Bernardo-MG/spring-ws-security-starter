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

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.domain.model.Role;

import lombok.extern.slf4j.Slf4j;

/**
 * Default user role service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultUserRoleService implements UserRoleService {

    private final RoleRepository       roleRepository;

    private final UserSpringRepository userRepository;

    private final UserRoleRepository   userRoleRepository;

    public DefaultUserRoleService(final UserSpringRepository userRepo, final RoleRepository roleRepo,
            final UserRoleRepository userRoleRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        userRoleRepository = Objects.requireNonNull(userRoleRepo);
        roleRepository = Objects.requireNonNull(roleRepo);
    }

    @Override
    public final Role addRole(final String username, final String role) {
        final UserRoleEntity       userRoleSample;
        final Optional<RoleEntity> readRole;
        final Optional<UserEntity> readUser;

        log.debug("Adding role {} to user {}", role, username);

        readUser = userRepository.findOneByUsername(username);

        if (readUser.isEmpty()) {
            throw new MissingUserUsernameException(username);
        }

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        userRoleSample = getUserRoleSample(readUser.get()
            .getId(),
            readRole.get()
                .getId());

        // Persist relationship
        userRoleRepository.save(userRoleSample);

        return toDto(readRole.get());
    }

    @Override
    public final Iterable<Role> getAvailableRoles(final String username, final Pageable pageable) {
        final int            count;
        final Iterable<Role> roles;

        count = roleRepository.countForUser(username);
        if (count == 0) {
            roles = roleRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
        } else {
            roles = roleRepository.findAvailableToUser(username, pageable)
                .map(this::toDto);
        }

        return roles;
    }

    @Override
    public final Iterable<Role> getRoles(final String username, final Pageable pageable) {
        log.debug("Getting roles for user {} and pagination {}", username, pageable);

        return roleRepository.findForUser(username, pageable)
            .map(this::toDto);
    }

    @Override
    public final Role removeRole(final String username, final String role) {
        final UserRoleEntity       userRoleSample;
        final Optional<RoleEntity> readRole;
        final Optional<UserEntity> readUser;

        log.debug("Removing role {} from user {}", username, role);

        readUser = userRepository.findOneByUsername(username);

        if (readUser.isEmpty()) {
            throw new MissingUserUsernameException(username);
        }

        readRole = roleRepository.findOneByName(role);

        if (readRole.isEmpty()) {
            throw new MissingRoleNameException(role);
        }

        userRoleSample = getUserRoleSample(readUser.get()
            .getId(),
            readRole.get()
                .getId());

        // Persist relationship
        userRoleRepository.delete(userRoleSample);

        return toDto(readRole.get());
    }

    private final UserRoleEntity getUserRoleSample(final long user, final long role) {
        return UserRoleEntity.builder()
            .withUserId(user)
            .withRoleId(role)
            .build();
    }

    private final Role toDto(final RoleEntity role) {
        return Role.builder()
            .withName(role.getName())
            .build();
    }

}
