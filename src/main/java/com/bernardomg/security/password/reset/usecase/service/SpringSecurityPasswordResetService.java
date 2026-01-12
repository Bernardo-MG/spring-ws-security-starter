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

package com.bernardomg.security.password.reset.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.password.reset.domain.event.PasswordResetEvent;
import com.bernardomg.security.password.reset.usecase.validation.EmailFormatRule;
import com.bernardomg.security.password.validation.PasswordResetHasStrongPasswordRule;
import com.bernardomg.security.user.domain.exception.DisabledUserException;
import com.bernardomg.security.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.domain.exception.InvalidTokenException;
import com.bernardomg.security.user.domain.exception.LockedUserException;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserTokenStatus;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

/**
 * Password recovery service which integrates with Spring Security.
 * <h2>Validations</h2>
 * <p>
 * If any of these fails, then the password change can't start.
 * <ul>
 * <li>Received email exists as a user</li>
 * <li>User should be enabled, and valid</li>
 * </ul>
 * If any of these fails, then the password change can't finalize.
 * <ul>
 * <li>The token is valid</li>
 * <li>Password received as the current password matches the actual current password</li>
 * </ul>
 * <h2></h2>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class SpringSecurityPasswordResetService implements PasswordResetService {

    /**
     * Logger for the class.
     */
    private static final Logger      log = LoggerFactory.getLogger(SpringSecurityPasswordResetService.class);

    /**
     * Event emitter.
     */
    private final EventEmitter       eventEmitter;

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder    passwordEncoder;

    /**
     * Token store for password reset tokens.
     */
    private final UserTokenStore     passwordResetTokenStore;

    /**
     * User details service, to find and validate users.
     */
    private final UserDetailsService userDetailsService;

    /**
     * User repository.
     */
    private final UserRepository     userRepository;

    /**
     * Change password validator.
     */
    private final Validator<String>  validatorChange;

    /**
     * Start password change validator.
     */
    private final Validator<String>  validatorStart;

    public SpringSecurityPasswordResetService(final UserRepository repo, final UserDetailsService userDetsService,
            final PasswordEncoder passEncoder, final UserTokenStore tStore, final EventEmitter eventEmit) {
        super();

        userRepository = Objects.requireNonNull(repo);
        userDetailsService = Objects.requireNonNull(userDetsService);
        passwordEncoder = Objects.requireNonNull(passEncoder);
        passwordResetTokenStore = Objects.requireNonNull(tStore);
        eventEmitter = Objects.requireNonNull(eventEmit);

        validatorChange = new FieldRuleValidator<>(new PasswordResetHasStrongPasswordRule());
        validatorStart = new FieldRuleValidator<>(new EmailFormatRule());
    }

    @Override
    @Transactional
    public final void changePassword(final String token, final String password) {
        final String username;
        final User   user;
        final String encodedPassword;

        log.trace("Changing password from token");

        passwordResetTokenStore.validate(token);

        log.debug("Validating new password");
        validatorChange.validate(password);

        username = passwordResetTokenStore.getUsername(token);

        log.debug("Applying requested password change to {}", username);

        user = getUserByUsername(username);

        authorizePasswordChange(user.username());

        encodedPassword = passwordEncoder.encode(password);
        userRepository.resetPassword(user.username(), encodedPassword);
        passwordResetTokenStore.consumeToken(token);

        log.trace("Changed password for {}", username);
    }

    @Override
    public final void startPasswordReset(final String email) {
        final User               user;
        final String             token;
        final PasswordResetEvent passwordResetEvent;

        // TODO: run async

        log.trace("Requested password recovery for {}", email);

        validatorStart.validate(email);

        user = getUserByEmail(email);

        // TODO: Reject authenticated users? Allow only password recovery for the anonymous user

        // Make sure the user can change the password
        authorizePasswordChange(user.username());

        // Revoke previous tokens
        log.debug("Revoking existing password reset tokens for {}", user.username());
        passwordResetTokenStore.revokeExistingTokens(user.username());

        // Register new token
        log.debug("Generating new token to reset password for {}", user.username());
        token = passwordResetTokenStore.createToken(user.username());

        passwordResetEvent = new PasswordResetEvent(this, user, token);
        eventEmitter.emit(passwordResetEvent);

        log.trace("Finished password recovery request for {}", email);
    }

    @Override
    public final UserTokenStatus validateToken(final String token) {
        final UserTokenStatus status;
        boolean               valid;
        String                username;

        log.trace("Validating password change token");

        try {
            passwordResetTokenStore.validate(token);
            valid = true;
        } catch (final InvalidTokenException ex) {
            valid = false;
        }

        try {
            username = passwordResetTokenStore.getUsername(token);
        } catch (final InvalidTokenException ex) {
            username = "";
        }

        status = new UserTokenStatus(username, valid);

        log.trace("Validated password change token with status {}", status);

        return status;
    }

    /**
     * Authenticates the password change attempt. If the user is not authenticated, then an exception is thrown.
     *
     * @param username
     *            username for which the password is changed
     */
    private final void authorizePasswordChange(final String username) {
        final UserDetails userDetails;

        userDetails = userDetailsService.loadUserByUsername(username);

        // Accepts users with expired credentials, as they have an expired password

        // TODO: This should be contained in a common class
        if (!userDetails.isAccountNonExpired()) {
            log.error("Can't reset password. User {} is expired", userDetails.getUsername());
            throw new ExpiredUserException(userDetails.getUsername());
        }
        if (!userDetails.isAccountNonLocked()) {
            log.error("Can't reset password. User {} is locked", userDetails.getUsername());
            throw new LockedUserException(userDetails.getUsername());
        }
        if (!userDetails.isEnabled()) {
            log.error("Can't reset password. User {} is disabled", userDetails.getUsername());
            throw new DisabledUserException(userDetails.getUsername());
        }

        log.debug("Can reset password for user {}", username);
    }

    private final User getUserByEmail(final String email) {
        final Optional<User> user;

        user = userRepository.findOneByEmail(email);

        // Validate the user exists
        if (!user.isPresent()) {
            log.error("Couldn't change password for email {}, as no user exists for it", email);
            throw new MissingUsernameException(email);
        }

        return user.get();
    }

    private final User getUserByUsername(final String username) {
        final Optional<User> user;

        user = userRepository.findOne(username);

        // Validate the user exists
        if (!user.isPresent()) {
            log.error("Couldn't change password for user {}, as it doesn't exist", username);
            throw new MissingUsernameException(username);
        }

        return user.get();
    }

}
