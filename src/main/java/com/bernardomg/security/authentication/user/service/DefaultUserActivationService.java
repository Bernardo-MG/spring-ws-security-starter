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

package com.bernardomg.security.authentication.user.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.exception.EnabledUserException;
import com.bernardomg.security.authentication.user.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.exception.LockedUserException;
import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.notification.UserNotificator;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.validation.RegisterUserValidator;
import com.bernardomg.security.authorization.token.exception.InvalidTokenException;
import com.bernardomg.security.authorization.token.model.UserTokenStatus;
import com.bernardomg.security.authorization.token.store.UserTokenStore;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Default user activation service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultUserActivationService implements UserActivationService {

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder       passwordEncoder;

    /**
     * Token processor.
     */
    private final UserTokenStore        tokenStore;

    /**
     * Message sender. Registering new users may require emails, or other kind of messaging.
     */
    private final UserNotificator       userNotificator;

    /**
     * User repository.
     */
    private final UserRepository        userRepository;

    /**
     * User registration validator.
     */
    private final Validator<UserEntity> validatorRegisterUser;

    public DefaultUserActivationService(final UserRepository userRepo, final UserNotificator mSender,
            final UserTokenStore tStore, final PasswordEncoder passEncoder) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        tokenStore = Objects.requireNonNull(tStore);
        passwordEncoder = Objects.requireNonNull(passEncoder);
        userNotificator = Objects.requireNonNull(mSender);

        validatorRegisterUser = new RegisterUserValidator(userRepo);
    }

    @Override
    public final User activateUser(final String token, final String password) {
        final String     username;
        final UserEntity user;
        final String     encodedPassword;

        // Validate token
        tokenStore.validate(token);

        // Acquire username
        username = tokenStore.getUsername(token);

        log.debug("Activating new user {}", username);

        user = getUserByUsername(username);

        validateActivation(user);

        user.setEnabled(true);
        user.setPasswordExpired(false);
        encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        userRepository.save(user);
        tokenStore.consumeToken(token);

        log.debug("Activated new user {}", username);

        return toDto(user);
    }

    @Override
    public final User registerNewUser(final String username, final String name, final String email) {
        final UserEntity userEntity;
        final UserEntity created;
        final String     token;

        log.debug("Registering new user {} with email {}", username, email);

        userEntity = UserEntity.builder()
            .withUsername(username)
            .withName(name)
            .withEmail(email)
            .build();

        validatorRegisterUser.validate(userEntity);

        // Trim strings
        userEntity.setName(userEntity.getName()
            .trim());
        userEntity.setUsername(userEntity.getUsername()
            .trim());
        userEntity.setEmail(userEntity.getEmail()
            .trim());

        // Remove case
        userEntity.setUsername(userEntity.getUsername()
            .toLowerCase());
        userEntity.setEmail(userEntity.getEmail()
            .toLowerCase());

        // TODO: Handle this better, disable until it has a password
        // TODO: Should be the DB default value
        userEntity.setPassword("");

        // Disabled by default
        userEntity.setEnabled(false);
        userEntity.setExpired(false);
        userEntity.setLocked(false);
        userEntity.setPasswordExpired(true);

        created = userRepository.save(userEntity);

        // Revoke previous tokens
        tokenStore.revokeExistingTokens(created.getUsername());

        // Register new token
        token = tokenStore.createToken(created.getUsername());

        // TODO: Handle through events
        userNotificator.sendUserRegisteredMessage(created.getEmail(), username, token);

        log.debug("Registered new user {} with email {}", username, email);

        return toDto(created);
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

    private final UserEntity getUserByUsername(final String username) {
        final Optional<UserEntity> user;

        user = userRepository.findOneByUsername(username);

        // Validate the user exists
        if (!user.isPresent()) {
            log.error("Couldn't activate new user {}, as it doesn't exist", username);
            throw new MissingUserUsernameException(username);
        }

        return user.get();
    }

    private final User toDto(final UserEntity user) {
        return User.builder()
            .withUsername(user.getUsername())
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.getEnabled())
            .withExpired(user.getExpired())
            .withLocked(user.getLocked())
            .withPasswordExpired(user.getPasswordExpired())
            .build();
    }

    /**
     * Checks whether the user can be activated. If the user can't be activated, then an exception is thrown.
     *
     * @param user
     *            user to activate
     */
    private final void validateActivation(final UserEntity user) {
        if (Boolean.TRUE.equals(user.getExpired())) {
            log.error("Can't activate new user. User {} is expired", user.getUsername());
            throw new ExpiredUserException(user.getUsername());
        }
        if (Boolean.TRUE.equals(user.getLocked())) {
            log.error("Can't activate new user. User {} is locked", user.getUsername());
            throw new LockedUserException(user.getUsername());
        }
        if (Boolean.TRUE.equals(user.getEnabled())) {
            log.error("Can't activate new user. User {} is already enabled", user.getUsername());
            throw new EnabledUserException(user.getUsername());
        }
    }

}
