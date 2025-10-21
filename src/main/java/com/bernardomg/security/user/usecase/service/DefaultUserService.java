/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.user.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.exception.MissingRoleException;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserQuery;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.usecase.validation.UserEmailFormatRule;
import com.bernardomg.security.user.usecase.validation.UserEmailNotExistsForAnotherRule;
import com.bernardomg.security.user.usecase.validation.UserEmailNotExistsRule;
import com.bernardomg.security.user.usecase.validation.UserRolesNotDuplicatedRule;
import com.bernardomg.security.user.usecase.validation.UserUsernameNotExistsRule;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

/**
 * Default user service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultUserService implements UserService {

    /**
     * Logger for the class.
     */
    private static final Logger   log = LoggerFactory.getLogger(DefaultUserService.class);

    /**
     * Role repository.
     */
    private final RoleRepository  roleRepository;

    /**
     * User repository.
     */
    private final UserRepository  userRepository;

    /**
     * User registration validator.
     */
    private final Validator<User> validatorCreateUser;

    /**
     * Update user validator.
     */
    private final Validator<User> validatorUpdateUser;

    public DefaultUserService(final UserRepository userRepo, final RoleRepository roleRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        roleRepository = Objects.requireNonNull(roleRepo);

        validatorCreateUser = new FieldRuleValidator<>(new UserEmailFormatRule(), new UserRolesNotDuplicatedRule(),
            new UserEmailNotExistsRule(userRepo), new UserUsernameNotExistsRule(userRepository));
        validatorUpdateUser = new FieldRuleValidator<>(new UserEmailFormatRule(), new UserRolesNotDuplicatedRule(),
            new UserEmailNotExistsForAnotherRule(userRepo));
    }

    @Override
    public final User create(final User user) {
        final User toCreate;
        final User created;

        log.trace("Creating user {} with email {} and name {}", user.username(), user.email(), user.name());

        // Verify the roles exists
        for (final Role role : user.roles()) {
            if (!roleRepository.exists(role.name())) {
                log.error("Missing role {}", role.name());
                throw new MissingRoleException(role.name());
            }
        }

        toCreate = User.newUser(user.username()
            .trim()
            .toLowerCase(LocaleContextHolder.getLocale()),
            user.email()
                .trim()
                .toLowerCase(LocaleContextHolder.getLocale()),
            user.name()
                .trim()
                .toLowerCase(LocaleContextHolder.getLocale()),
            user.roles());

        validatorCreateUser.validate(toCreate);

        created = userRepository.saveNewUser(toCreate);

        log.trace("Created user {} with email {} and name {}", created.username(), created.email(), user.name());

        return created;
    }

    @Override
    public final User delete(final String username) {
        final User user;

        log.trace("Deleting user {}", username);

        user = userRepository.findOne(username)
            .orElseThrow(() -> {
                log.error("Missing user {}", username);
                throw new MissingUsernameException(username);
            });

        userRepository.delete(username);

        log.trace("Deleted user {}", username);

        return user;
    }

    @Override
    public final Page<User> getAll(final UserQuery query, final Pagination pagination, final Sorting sorting) {
        final Page<User> users;

        log.trace("Reading users with sample {}, pagination {} and sorting {}", query, pagination, sorting);

        users = userRepository.findAll(query, pagination, sorting);

        log.trace("Read users with sample {}, pagination {} and sorting {}", query, pagination, sorting);

        return users;
    }

    @Override
    public final Optional<User> getOne(final String username) {
        final Optional<User> user;

        log.trace("Reading user {}", username);

        user = userRepository.findOne(username);
        if (user.isEmpty()) {
            log.error("Missing user {}", username);
            throw new MissingUsernameException(username);
        }

        log.trace("Read user {}", username);

        return user;
    }

    @Override
    public final User update(final User user) {
        final User existing;
        final User toSave;
        final User updated;

        log.trace("Updating user {} using data {}", user.username(), user);

        // Verify the user exists
        existing = userRepository.findOne(user.username())
            .orElseThrow(() -> {
                log.error("Missing user {}", user.username());
                throw new MissingUsernameException(user.username());
            });

        // Verify the roles exists
        for (final Role role : user.roles()) {
            if (!roleRepository.exists(role.name())) {
                log.error("Missing role {}", role.name());
                throw new MissingRoleException(role.name());
            }
        }

        validatorUpdateUser.validate(user);

        toSave = new User(user.email(), existing.username(), user.name(), user.enabled(), existing.notExpired(),
            existing.notLocked(), user.passwordNotExpired(), user.roles());

        updated = userRepository.save(toSave);

        log.trace("Updated user {} using data {}", user.username(), user);

        return updated;
    }

}
