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

package com.bernardomg.security.password.change.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.exception.DisabledUserException;
import com.bernardomg.security.authentication.user.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.exception.LockedUserException;
import com.bernardomg.security.authentication.user.exception.UserNotFoundException;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.password.exception.InvalidPasswordChangeException;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SpringSecurityPasswordChangeService implements PasswordChangeService {

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

    public SpringSecurityPasswordChangeService(@NonNull final UserRepository userRepo,
            @NonNull final UserDetailsService userDetsService, @NonNull final PasswordEncoder passEncoder) {
        super();

        repository = userRepo;
        userDetailsService = userDetsService;
        passwordEncoder = passEncoder;
    }

    @Override
    public final void changePasswordForUserInSession(final String oldPassword, final String newPassword) {
        final UserEntity  user;
        final String      encodedPassword;
        final String      username;
        final UserDetails userDetails;

        username = getCurrentUsername();

        log.debug("Changing password for user {}", username);

        user = getUser(username);

        // TODO: Avoid this second query
        userDetails = userDetailsService.loadUserByUsername(username);

        // TODO: Move to validator
        validatePassword(userDetails, oldPassword);

        // Make sure the user can change the password
        authorizePasswordChange(userDetails);

        encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setPasswordExpired(false);

        repository.save(user);

        log.debug("Changed password for user {}", username);
    }

    /**
     * Authenticates the password change attempt. If the user is not authenticated, then an exception is thrown.
     *
     * @param user
     *            user for which the password is changed
     */
    private final void authorizePasswordChange(final UserDetails user) {
        // TODO: This should be contained in a common class
        if (!user.isAccountNonExpired()) {
            log.error("Can't reset password. User {} is expired", user.getUsername());
            throw new ExpiredUserException(user.getUsername());
        }
        if (!user.isAccountNonLocked()) {
            log.error("Can't reset password. User {} is locked", user.getUsername());
            throw new LockedUserException(user.getUsername());
        }
        if (!user.isEnabled()) {
            log.error("Can't reset password. User {} is disabled", user.getUsername());
            throw new DisabledUserException(user.getUsername());
        }
    }

    private final String getCurrentUsername() {
        final Authentication auth;

        auth = SecurityContextHolder.getContext()
            .getAuthentication();
        if ((auth == null) || (!auth.isAuthenticated())) {
            throw new InvalidPasswordChangeException("No user authenticated", "");
        }

        return auth.getName();
    }

    private final UserEntity getUser(final String username) {
        final Optional<UserEntity> user;

        user = repository.findOneByUsername(username);

        // Validate the user exists
        if (!user.isPresent()) {
            log.error("Couldn't change password for user {}, as it doesn't exist", username);
            throw new UserNotFoundException(username);
        }

        return user.get();
    }

    private final void validatePassword(final UserDetails userDetails, final String oldPassword) {
        final FieldFailure failure;

        // Verify the current password matches the original one
        if (!passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
            log.warn("Received a password which doesn't match the one stored for username {}",
                userDetails.getUsername());
            failure = FieldFailure.of("oldPassword", "notMatch", oldPassword);
            throw new FieldFailureException(List.of(failure));
        }
    }

}
