
package com.bernardomg.security.login.adapter.inbound.spring;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public final class SpringValidLoginPredicate implements BiPredicate<String, String> {

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder    passwordEncoder;

    /**
     * User details service, to find and validate users.
     */
    private final UserDetailsService userDetailsService;

    public SpringValidLoginPredicate(final UserDetailsService userDetService, final PasswordEncoder passEncoder) {
        super();

        userDetailsService = Objects.requireNonNull(userDetService);
        passwordEncoder = Objects.requireNonNull(passEncoder);
    }

    @Override
    public final boolean test(final String username, final String password) {
        final boolean         valid;
        Optional<UserDetails> details;

        // TODO: Throw exceptions

        // Find the user
        try {
            details = Optional
                .ofNullable(userDetailsService.loadUserByUsername(username.toLowerCase(Locale.getDefault())));
        } catch (final UsernameNotFoundException e) {
            details = Optional.empty();
        }

        if (details.isEmpty()) {
            // No user found for username
            log.debug("No user for username {}. Failed login", username);
            valid = false;
        } else if (isValid(details.get())) {
            // User exists
            // Validate password
            valid = passwordEncoder.matches(password, details.get()
                .getPassword());
            if (!valid) {
                log.debug("Received a password which doesn't match the one stored for username {}. Failed login",
                    username);
            }
        } else {
            // Invalid user
            log.debug("User {} is in an invalid state. Failed login", username);
            if (!details.get()
                .isAccountNonExpired()) {
                log.debug("User {} account expired", username);
            }
            if (!details.get()
                .isAccountNonLocked()) {
                log.debug("User {} account is locked", username);
            }
            if (!details.get()
                .isCredentialsNonExpired()) {
                log.debug("User {} credentials expired", username);
            }
            if (!details.get()
                .isEnabled()) {
                log.debug("User {} is disabled", username);
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
