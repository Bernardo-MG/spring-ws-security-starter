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

package com.bernardomg.security.usecase.password.change.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.domain.password.change.exception.InvalidPasswordChangeException;
import com.bernardomg.security.domain.user.exception.DisabledUserException;
import com.bernardomg.security.domain.user.exception.ExpiredUserException;
import com.bernardomg.security.domain.user.exception.LockedUserException;
import com.bernardomg.security.domain.user.exception.MissingUsernameException;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.password.validation.PasswordResetHasStrongPasswordRule;
import com.bernardomg.security.usecase.session.UsernameInSessionProvider;
import com.bernardomg.validation.domain.exception.FieldFailureException;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

import jakarta.transaction.Transactional;

/**
 * Password change service based on Spring security.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultPasswordChangeService implements PasswordChangeService {

    /**
     * Logger for the class.
     */
    private static final Logger             log = LoggerFactory.getLogger(DefaultPasswordChangeService.class);

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncrypter         passwordEncrypter;

    /**
     * User repository.
     */
    private final UserRepository            repository;

    private final UsernameInSessionProvider usernameInSessionProvider;

    /**
     * Change password validator.
     */
    private final Validator<String>         validatorChange;

    public DefaultPasswordChangeService(final UserRepository userRepo, final PasswordEncrypter passEncrypter,
            final UsernameInSessionProvider usernameInSessionProv) {
        super();

        repository = Objects.requireNonNull(userRepo);
        passwordEncrypter = Objects.requireNonNull(passEncrypter);
        usernameInSessionProvider = Objects.requireNonNull(usernameInSessionProv);

        validatorChange = new FieldRuleValidator<>(new PasswordResetHasStrongPasswordRule());

        // TODO: make independent from Spring
    }

    @Override
    public final void changePasswordForUserInSession(final String oldPassword, final String newPassword) {
        final Optional<String> username;
        final Optional<User>   user;
        final String           encodedPassword;

        // TODO: handle missing username
        username = usernameInSessionProvider.getCurrentUsername();
        if (username.isEmpty()) {
            throw new InvalidPasswordChangeException("No user authenticated", "");
        }

        log.trace("Changing password for user {}", username);

        // Validate the user exists
        user = repository.findOne(username.get());
        if (user.isEmpty()) {
            // TODO: Is this exception being hid?
            log.error("Missing user {}", username);
            throw new MissingUsernameException(username.get());
        }

        // TODO: Move to validator
        validatePassword(user.get(), oldPassword);

        log.trace("Validating new password");
        validatorChange.validate(newPassword);

        // Make sure the user can change the password
        authorizePasswordChange(user.get());

        encodedPassword = passwordEncrypter.encrypt(newPassword);
        repository.resetPassword(username.get(), encodedPassword);

        log.trace("Changed password for user {}", username.get());
    }

    /**
     * Authenticates the password change attempt. If the user is not authenticated, then an exception is thrown.
     *
     * @param user
     *            user for which the password is changed
     */
    private final void authorizePasswordChange(final User user) {

        // Accepts users with expired credentials, as they have an expired password

        // TODO: This should be contained in a common class
        if (!user.notExpired()) {
            log.error("User {} is expired", user.username());
            throw new ExpiredUserException(user.username());
        }
        if (!user.notLocked()) {
            log.error("User {} is locked", user.username());
            throw new LockedUserException(user.username());
        }
        if (!user.enabled()) {
            log.error("User {} is disabled", user.username());
            throw new DisabledUserException(user.username());
        }
    }

    private final void validatePassword(final User user, final String oldPassword) {
        final FieldFailure failure;
        final String       password;

        // Verify the current password matches the original one
        password = repository.findPassword(user.username())
            .get();
        if (!passwordEncrypter.matches(oldPassword, password)) {
            log.error("Received a password which doesn't match the one stored for username {}", user.username());
            failure = new FieldFailure("notMatch", "oldPassword", oldPassword);
            throw new FieldFailureException(failure);
        }
    }

}
