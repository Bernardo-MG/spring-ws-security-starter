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

package com.bernardomg.security.user.data.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.role.domain.exception.MissingRoleException;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.data.domain.exception.MissingUserException;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.model.UserQuery;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.data.usecase.validation.UserEmailNotExistsForAnotherRule;
import com.bernardomg.security.user.data.usecase.validation.UserEmailNotExistsRule;
import com.bernardomg.security.user.data.usecase.validation.UserRolesNotDuplicatedRule;
import com.bernardomg.security.user.data.usecase.validation.UserUsernameNotExistsRule;
import com.bernardomg.security.user.notification.usecase.notificator.UserNotificator;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;
import com.bernardomg.validation.validator.FieldRuleValidator;
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
     * Token processor.
     */
    private final UserTokenStore  tokenStore;

    /**
     * Message sender. Registering new users may require emails, or other kind of messaging.
     */
    private final UserNotificator userNotificator;

    /**
     * User repository.
     */
    private final UserRepository  userRepository;

    /**
     * User registration validator.
     */
    private final Validator<User> validatorRegisterUser;

    /**
     * Update user validator.
     */
    private final Validator<User> validatorUpdateUser;

    public DefaultUserService(final UserRepository userRepo, final RoleRepository roleRepo,
            final UserNotificator userNotf, final UserTokenStore tStore) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        roleRepository = Objects.requireNonNull(roleRepo);
        userNotificator = Objects.requireNonNull(userNotf);
        tokenStore = Objects.requireNonNull(tStore);

        validatorRegisterUser = new FieldRuleValidator<>(new UserEmailNotExistsRule(userRepo),
            new UserUsernameNotExistsRule(userRepository));
        validatorUpdateUser = new FieldRuleValidator<>(new UserEmailNotExistsForAnotherRule(userRepo),
            new UserRolesNotDuplicatedRule());
    }

    @Override
    public final void delete(final String username) {
        log.debug("Deleting user {}", username);

        if (!userRepository.exists(username)) {
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
        final Optional<User> user;

        log.debug("Reading user {}", username);

        user = userRepository.findOne(username);
        if (user.isEmpty()) {
            log.error("Missing user {}", username);
            throw new MissingUserException(username);
        }

        return user;
    }

    @Override
    public final User registerNewUser(final String username, final String name, final String email) {
        final User   user;
        final User   created;
        final String token;

        log.debug("Registering new user {} with email {}", username, email);

        user = User.newUser(username, email, name);

        validatorRegisterUser.validate(user);

        created = userRepository.newUser(user);

        // Revoke previous tokens
        tokenStore.revokeExistingTokens(created.username());

        // Register new token
        token = tokenStore.createToken(created.username());

        // TODO: Handle through events
        userNotificator.sendUserRegisteredMessage(created.email(), username, token);

        log.debug("Registered new user {} with email {}", username, email);

        return created;
    }

    @Override
    public final User update(final User user) {
        final User existing;
        final User toSave;

        log.debug("Updating user {} using data {}", user.username(), user);

        // Verify the user exists
        existing = userRepository.findOne(user.username())
            .orElseThrow(() -> {
                log.error("Missing user {}", user.username());
                throw new MissingUserException(user.username());
            });

        // Verify the roles exists
        for (final Role role : user.roles()) {
            if (!roleRepository.exists(role.name())) {
                log.error("Missing role {}", role.name());
                throw new MissingRoleException(role.name());
            }
        }

        validatorUpdateUser.validate(user);

        toSave = User.builder()
            // Can't change these fields
            .withUsername(existing.username())
            .withExpired(existing.expired())
            .withLocked(existing.locked())
            // These fields are allowed to change
            .withName(user.name())
            .withEmail(user.email())
            .withEnabled(user.enabled())
            .withPasswordExpired(user.passwordExpired())
            .withRoles(user.roles())
            .build();

        return userRepository.save(toSave);
    }

}
