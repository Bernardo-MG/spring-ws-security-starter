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

package com.bernardomg.security.password.change.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.password.change.domain.exception.InvalidPasswordChangeException;
import com.bernardomg.security.password.validation.PasswordResetHasStrongPasswordRule;
import com.bernardomg.security.user.domain.exception.DisabledUserException;
import com.bernardomg.security.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.domain.exception.LockedUserException;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.validation.domain.exception.FieldFailureException;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

/**
 * Password change service based on Spring security.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class SpringSecurityPasswordChangeService implements PasswordChangeService {

    /**
     * Logger for the class.
     */
    private static final Logger     log = LoggerFactory.getLogger(SpringSecurityPasswordChangeService.class);

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder   passwordEncoder;

    /**
     * User repository.
     */
    private final UserRepository    repository;

    /**
     * Change password validator.
     */
    private final Validator<String> validatorChange;

    public SpringSecurityPasswordChangeService(final UserRepository userRepo, final PasswordEncoder passEncoder) {
        super();

        repository = Objects.requireNonNull(userRepo);
        passwordEncoder = Objects.requireNonNull(passEncoder);

        validatorChange = new FieldRuleValidator<>(new PasswordResetHasStrongPasswordRule());

        // TODO: make independent from Spring
    }

    @Override
    public final void changePasswordForUserInSession(final String oldPassword, final String newPassword) {
        final String         username;
        final Optional<User> user;

        username = getCurrentUsername();

        log.trace("Changing password for user {}", username);

        // Validate the user exists
        user = repository.findOne(username);
        if (user.isEmpty()) {
            // TODO: Is this exception being hid?
            log.error("Missing user {}", username);
            throw new MissingUsernameException(username);
        }

        // TODO: Move to validator
        validatePassword(user.get(), oldPassword);

        log.trace("Validating new password");
        validatorChange.validate(newPassword);

        // Make sure the user can change the password
        authorizePasswordChange(user.get());

        repository.resetPassword(username, newPassword);

        log.trace("Changed password for user {}", username);
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

    private final String getCurrentUsername() {
        final Authentication auth;

        auth = SecurityContextHolder.getContext()
            .getAuthentication();
        if ((auth == null) || (!auth.isAuthenticated())) {
            // TODO: Use an exception matching the actual error
            log.error("Missing authentication in session");
            throw new InvalidPasswordChangeException("No user authenticated", "");
        }

        return auth.getName();
    }

    private final void validatePassword(final User user, final String oldPassword) {
        final FieldFailure failure;
        final String       password;

        // Verify the current password matches the original one
        password = repository.findPassword(user.username())
            .get();
        if (!passwordEncoder.matches(oldPassword, password)) {
            log.error("Received a password which doesn't match the one stored for username {}", user.username());
            failure = new FieldFailure("notMatch", "oldPassword", oldPassword);
            throw new FieldFailureException(failure);
        }
    }

}
