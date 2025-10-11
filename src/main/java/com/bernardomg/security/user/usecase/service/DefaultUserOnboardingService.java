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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.password.validation.PasswordResetHasStrongPasswordRule;
import com.bernardomg.security.role.domain.exception.MissingRoleException;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.domain.event.UserInvitationEvent;
import com.bernardomg.security.user.domain.exception.InvalidTokenException;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserTokenStatus;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.security.user.usecase.validation.UserEmailFormatRule;
import com.bernardomg.security.user.usecase.validation.UserEmailNotExistsRule;
import com.bernardomg.security.user.usecase.validation.UserRolesNotDuplicatedRule;
import com.bernardomg.security.user.usecase.validation.UserUsernameNotExistsRule;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

/**
 * Default user activation service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultUserOnboardingService implements UserOnboardingService {

    /**
     * Logger for the class.
     */
    private static final Logger     log = LoggerFactory.getLogger(DefaultUserOnboardingService.class);

    /**
     * Event emitter.
     */
    private final EventEmitter      eventEmitter;

    /**
     * Role repository.
     */
    private final RoleRepository    roleRepository;

    /**
     * Token processor.
     */
    private final UserTokenStore    tokenStore;

    /**
     * User repository.
     */
    private final UserRepository    userRepository;

    /**
     * User activation validator.
     */
    private final Validator<String> validatorActivate;

    /**
     * User registration validator.
     */
    private final Validator<User>   validatorInvite;

    public DefaultUserOnboardingService(final UserRepository userRepo, final RoleRepository roleRepo,
            final UserTokenStore tStore, final EventEmitter eventEmit) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        roleRepository = Objects.requireNonNull(roleRepo);
        tokenStore = Objects.requireNonNull(tStore);
        eventEmitter = Objects.requireNonNull(eventEmit);

        validatorActivate = new FieldRuleValidator<>(new PasswordResetHasStrongPasswordRule());
        validatorInvite = new FieldRuleValidator<>(new UserEmailFormatRule(), new UserRolesNotDuplicatedRule(),
            new UserEmailNotExistsRule(userRepo), new UserUsernameNotExistsRule(userRepository));
    }

    @Override
    public final User activateUser(final String token, final String password) {
        final String username;
        final User   user;
        final User   saved;

        log.trace("Activating new user");

        // Validate token
        tokenStore.validate(token);

        // Validate password
        validatorActivate.validate(password.trim());

        // Acquire username
        username = tokenStore.getUsername(token);

        log.debug("Activating new user {}", username);

        user = userRepository.findOne(username)
            .orElseThrow(() -> {
                log.error("Missing user {}", username);
                throw new MissingUsernameException(username);
            });

        // TODO: validate somehow that it is actually a new user
        user.checkStatus();

        saved = userRepository.activate(username, password.trim());
        tokenStore.consumeToken(token);

        log.trace("Activated new user {}", username);

        return saved;
    }

    @Override
    public final User inviteUser(final User user) {
        final User                toCreate;
        final User                created;
        final String              token;
        final UserInvitationEvent userInvitationEvent;

        log.trace("Inviting new user {} with email {} and name {}", user.username(), user.email(), user.name());

        // Verify the roles exists
        for (final Role role : user.roles()) {
            if (!roleRepository.exists(role.name())) {
                log.error("Missing role {}", role.name());
                throw new MissingRoleException(role.name());
            }
        }

        toCreate = User.newUser(user.username()
            .trim()
            .toLowerCase(),
            user.email()
                .trim()
                .toLowerCase(),
            user.name()
                .trim()
                .toLowerCase(),
            user.roles());

        validatorInvite.validate(toCreate);

        created = userRepository.saveNewUser(toCreate);

        // Register new token for activation
        token = tokenStore.createToken(created.username());

        userInvitationEvent = new UserInvitationEvent(this, created.email(), created.username(), token);
        eventEmitter.emit(userInvitationEvent);

        log.trace("Invited new user {} with email {} and name {}", created.username(), created.email(), user.name());

        return created;
    }

    @Override
    public final UserTokenStatus validateToken(final String token) {
        final UserTokenStatus status;
        final String          username;
        boolean               valid;

        log.trace("Validating user activation token");

        try {
            // TODO: maybe return a boolean instead of throwing an exception
            tokenStore.validate(token);
            valid = true;
        } catch (final InvalidTokenException ex) {
            valid = false;
        }
        username = tokenStore.getUsername(token);

        status = new UserTokenStatus(username, valid);

        log.trace("Validated user activation token with status {}", status);

        return status;
    }

}
