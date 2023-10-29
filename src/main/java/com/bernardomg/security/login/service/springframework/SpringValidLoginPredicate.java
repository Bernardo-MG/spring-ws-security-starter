
package com.bernardomg.security.login.service.springframework;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.login.model.request.LoginRequest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Login validator which integrates with Spring Security. It makes use of {@link UserDetailsService} to find the user
 * which tries to log in.
 * <h2>Validations</h2>
 * <p>
 * If any of these fails, then the log in fails.
 * <ul>
 * <li>Received username exists as a user</li>
 * <li>Received password matchs the one encrypted for the user</li>
 * <li>User should be enabled, and valid</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class SpringValidLoginPredicate implements Predicate<LoginRequest> {

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder    passwordEncoder;

    /**
     * User details service, to find and validate users.
     */
    private final UserDetailsService userDetailsService;

    public SpringValidLoginPredicate(@NonNull final UserDetailsService userDetService,
            @NonNull final PasswordEncoder passEncoder) {
        super();

        userDetailsService = userDetService;
        passwordEncoder = passEncoder;
    }

    @Override
    public final boolean test(final LoginRequest login) {
        final boolean         valid;
        Optional<UserDetails> details;

        // TODO: Throw exceptions

        // Find the user
        try {
            details = Optional.ofNullable(userDetailsService.loadUserByUsername(login.getUsername()
                .toLowerCase(Locale.getDefault())));
        } catch (final UsernameNotFoundException e) {
            details = Optional.empty();
        }

        if (details.isEmpty()) {
            // No user found for username
            log.debug("No user for username {}. Failed login", login.getUsername());
            valid = false;
        } else if (isValid(details.get())) {
            // User exists
            // Validate password
            valid = passwordEncoder.matches(login.getPassword(), details.get()
                .getPassword());
            if (!valid) {
                log.debug("Received a password which doesn't match the one stored for username {}. Failed login",
                    login.getUsername());
            }
        } else {
            // Invalid user
            log.debug("User {} is in an invalid state. Failed login", login.getUsername());
            if (!details.get()
                .isAccountNonExpired()) {
                log.debug("User {} account expired", login.getUsername());
            }
            if (!details.get()
                .isAccountNonLocked()) {
                log.debug("User {} account is locked", login.getUsername());
            }
            if (!details.get()
                .isCredentialsNonExpired()) {
                log.debug("User {} credentials expired", login.getUsername());
            }
            if (!details.get()
                .isEnabled()) {
                log.debug("User {} is disabled", login.getUsername());
            }
            valid = false;
        }

        return valid;
    }

    /**
     * Checks if the user is valid. This means it has no flag marking it as not usable.
     *
     * @param userDetails
     *            user the check
     * @return {@code true} if the user is valid, {@code false} otherwise
     */
    private final boolean isValid(final UserDetails userDetails) {
        return userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked()
                && userDetails.isCredentialsNonExpired() && userDetails.isEnabled();
    }

}
