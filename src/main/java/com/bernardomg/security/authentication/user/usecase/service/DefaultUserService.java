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

package com.bernardomg.security.authentication.user.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.usecase.validation.UpdateUserValidator;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleException;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.validation.validator.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Default user service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
@Transactional
public final class DefaultUserService implements UserService {

    /**
     * Role repository.
     */
    private final RoleRepository  roleRepository;

    /**
     * User repository.
     */
    private final UserRepository  userRepository;

    /**
     * Update user validator.
     */
    private final Validator<User> validatorUpdateUser;

    public DefaultUserService(final UserRepository userRepo, final RoleRepository roleRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        roleRepository = Objects.requireNonNull(roleRepo);

        validatorUpdateUser = new UpdateUserValidator(userRepo);
    }

    @Override
    public final void delete(final String username) {
        final boolean exists;

        log.debug("Deleting user {}", username);

        exists = userRepository.exists(username);
        if (!exists) {
            log.error("Missing user {}", username);
            throw new MissingUserException(username);
        }

        userRepository.delete(username);
    }

    @Override
    public final Iterable<User> getAll(final UserQuery query, final Pageable page) {
        log.debug("Reading users with sample {} and pagination {}", query, page);

        return userRepository.findAll(query, page);
    }

    @Override
    public final Optional<User> getOne(final String username) {
        final boolean exists;

        log.debug("Reading user {}", username);

        exists = userRepository.exists(username);
        if (!exists) {
            log.error("Missing user {}", username);
            throw new MissingUserException(username);
        }

        return userRepository.findOne(username);
    }

    @Override
    public final User update(final User user) {
        final Optional<User> existing;
        final User           toSave;

        log.debug("Updating user {} using data {}", user.getUsername(), user);

        // Verify the user exists
        existing = userRepository.findOne(user.getUsername());
        if (existing.isEmpty()) {
            log.error("Missing user {}", user.getUsername());
            throw new MissingUserException(user.getUsername());
        }

        // Verify the roles exists
        for (final Role role : user.getRoles()) {
            if (!roleRepository.exists(role.getName())) {
                log.error("Missing role {}", role.getName());
                throw new MissingRoleException(role.getName());
            }
        }

        validatorUpdateUser.validate(user);

        toSave = User.builder()
            // Can't change these fields
            .withUsername(existing.get()
                .getUsername())
            .withExpired(existing.get()
                .isExpired())
            .withLocked(existing.get()
                .isLocked())
            // These fields are allowed to change
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.isEnabled())
            .withPasswordExpired(user.isPasswordExpired())
            .withRoles(user.getRoles())
            .build();

        return userRepository.save(toSave);
    }

}
