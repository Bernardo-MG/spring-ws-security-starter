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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authentication.user.domain.exception.EnabledUserException;
import com.bernardomg.security.authentication.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.domain.exception.LockedUserException;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.usecase.notification.UserNotificator;
import com.bernardomg.security.authentication.user.usecase.validation.RegisterUserValidator;
import com.bernardomg.security.authorization.token.domain.exception.InvalidTokenException;
import com.bernardomg.security.authorization.token.domain.model.UserTokenStatus;
import com.bernardomg.security.authorization.token.usecase.store.UserTokenStore;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Default user activation service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
@Transactional
public final class DefaultUserActivationService implements UserActivationService {

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

    public DefaultUserActivationService(final UserRepository userRepo, final UserNotificator mSender,
            final UserTokenStore tStore) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        tokenStore = Objects.requireNonNull(tStore);
        userNotificator = Objects.requireNonNull(mSender);

        validatorRegisterUser = new RegisterUserValidator(userRepo);
    }

    @Override
    public final User activateUser(final String token, final String password) {
        final String username;
        final User   user;
        final User   activated;
        final User   saved;

        // Validate token
        tokenStore.validate(token);

        // Acquire username
        username = tokenStore.getUsername(token);

        log.debug("Activating new user {}", username);

        user = getUserByUsername(username);

        validateActivation(user);

        activated = User.builder()
            .withUsername(user.getUsername())
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withRoles(user.getRoles())
            .withExpired(user.isExpired())
            .withLocked(user.isLocked())
            // Enable user
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();

        saved = userRepository.save(activated, password);
        tokenStore.consumeToken(token);

        log.debug("Activated new user {}", username);

        return saved;
    }

    @Override
    public final User registerNewUser(final String username, final String name, final String email) {
        final User   user;
        final User   created;
        final String token;

        log.debug("Registering new user {} with email {}", username, email);

        user = User.builder()
            .withUsername(username)
            .withName(name)
            .withEmail(email)
            .withRoles(List.of())
            // Password expired by default
            .withPasswordExpired(true)
            .build();

        validatorRegisterUser.validate(user);

        created = userRepository.save(user, "");

        // Revoke previous tokens
        tokenStore.revokeExistingTokens(created.getUsername());

        // Register new token
        token = tokenStore.createToken(created.getUsername());

        // TODO: Handle through events
        userNotificator.sendUserRegisteredMessage(created.getEmail(), username, token);

        log.debug("Registered new user {} with email {}", username, email);

        return created;
    }

    @Override
    public final UserTokenStatus validateToken(final String token) {
        final String username;
        boolean      valid;

        try {
            tokenStore.validate(token);
            valid = true;
        } catch (final InvalidTokenException ex) {
            valid = false;
        }
        username = tokenStore.getUsername(token);

        return UserTokenStatus.builder()
            .withValid(valid)
            .withUsername(username)
            .build();
    }

    private final User getUserByUsername(final String username) {
        final Optional<User> user;

        user = userRepository.findOne(username);

        // Validate the user exists
        if (!user.isPresent()) {
            log.error("Couldn't activate new user {}, as it doesn't exist", username);
            throw new MissingUserException(username);
        }

        return user.get();
    }

    /**
     * Checks whether the user can be activated. If the user can't be activated, then an exception is thrown.
     *
     * @param user
     *            user to activate
     */
    private final void validateActivation(final User user) {
        if (user.isExpired()) {
            log.error("Can't activate new user. User {} is expired", user.getUsername());
            throw new ExpiredUserException(user.getUsername());
        }
        if (user.isLocked()) {
            log.error("Can't activate new user. User {} is locked", user.getUsername());
            throw new LockedUserException(user.getUsername());
        }
        if (user.isEnabled()) {
            log.error("Can't activate new user. User {} is already enabled", user.getUsername());
            throw new EnabledUserException(user.getUsername());
        }
    }

}
