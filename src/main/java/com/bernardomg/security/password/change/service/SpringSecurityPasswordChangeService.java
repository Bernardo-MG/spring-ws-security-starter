
package com.bernardomg.security.password.change.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.exception.UserDisabledException;
import com.bernardomg.security.authentication.user.exception.UserExpiredException;
import com.bernardomg.security.authentication.user.exception.UserLockedException;
import com.bernardomg.security.authentication.user.exception.UserNotFoundException;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
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
        final PersistentUser user;
        final String         encodedPassword;
        final String         username;
        final UserDetails    userDetails;

        username = getCurrentUsername();

        log.debug("Changing password for user {}", username);

        user = getUser(username);

        // TODO: Avoid this second query
        userDetails = userDetailsService.loadUserByUsername(username);

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
            throw new UserExpiredException(user.getUsername());
        }
        if (!user.isAccountNonLocked()) {
            log.error("Can't reset password. User {} is locked", user.getUsername());
            throw new UserLockedException(user.getUsername());
        }
        if (!user.isEnabled()) {
            log.error("Can't reset password. User {} is disabled", user.getUsername());
            throw new UserDisabledException(user.getUsername());
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

    private final PersistentUser getUser(final String username) {
        final Optional<PersistentUser> user;

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
