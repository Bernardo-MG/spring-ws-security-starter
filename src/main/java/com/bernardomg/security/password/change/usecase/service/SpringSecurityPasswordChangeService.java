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

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.password.change.domain.exception.InvalidPasswordChangeException;
import com.bernardomg.security.user.data.domain.exception.DisabledUserException;
import com.bernardomg.security.user.data.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.data.domain.exception.LockedUserException;
import com.bernardomg.security.user.data.domain.exception.MissingUserException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.validation.domain.exception.FieldFailureException;
import com.bernardomg.validation.domain.model.FieldFailure;

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
    private static final Logger      log = LoggerFactory.getLogger(SpringSecurityPasswordChangeService.class);

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder    passwordEncoder;

    /**
     * User repository.
     */
    private final UserRepository     repository;

    /**
     * User details service, to find and validate users.
     */
    private final UserDetailsService userDetailsService;

    public SpringSecurityPasswordChangeService(final UserRepository userRepo, final UserDetailsService userDetsService,
            final PasswordEncoder passEncoder) {
        super();

        repository = Objects.requireNonNull(userRepo);
        userDetailsService = Objects.requireNonNull(userDetsService);
        passwordEncoder = Objects.requireNonNull(passEncoder);
    }

    @Override
    public final void changePasswordForUserInSession(final String oldPassword, final String newPassword) {
        final String      username;
        final UserDetails userDetails;

        username = getCurrentUsername();

        log.debug("Changing password for user {}", username);

        // Validate the user exists
        if (!repository.exists(username)) {
            // TODO: Is this exception being hid?
            log.error("Missing user {}", username);
            throw new MissingUserException(username);
        }

        // TODO: Avoid this second query
        userDetails = userDetailsService.loadUserByUsername(username);

        // TODO: Move to validator
        validatePassword(userDetails, oldPassword);

        // Make sure the user can change the password
        authorizePasswordChange(userDetails);

        repository.resetPassword(username, newPassword);

        log.debug("Changed password for user {}", username);
    }

    /**
     * Authenticates the password change attempt. If the user is not authenticated, then an exception is thrown.
     *
     * @param user
     *            user for which the password is changed
     */
    private final void authorizePasswordChange(final UserDetails user) {

        // Accepts users with expired credentials, as they have an expired password

        // TODO: This should be contained in a common class
        if (!user.isAccountNonExpired()) {
            log.error("User {} is expired", user.getUsername());
            throw new ExpiredUserException(user.getUsername());
        }
        if (!user.isAccountNonLocked()) {
            log.error("User {} is locked", user.getUsername());
            throw new LockedUserException(user.getUsername());
        }
        if (!user.isEnabled()) {
            log.error("User {} is disabled", user.getUsername());
            throw new DisabledUserException(user.getUsername());
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

    private final void validatePassword(final UserDetails userDetails, final String oldPassword) {
        final FieldFailure failure;

        // Verify the current password matches the original one
        if (!passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
            log.warn("Received a password which doesn't match the one stored for username {}",
                userDetails.getUsername());
            failure = new FieldFailure("notMatch", "oldPassword", oldPassword);
            throw new FieldFailureException(userDetails, List.of(failure));
        }
    }

}
