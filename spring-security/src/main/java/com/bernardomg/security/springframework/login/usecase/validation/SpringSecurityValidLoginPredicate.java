
package com.bernardomg.security.springframework.login.usecase.validation;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.domain.login.model.Credentials;

/**
 * Login validator which integrates with Spring Security. It makes use of {@link UserDetailsService} to find the user
 * which tries to log in.
 * <h2>Validations</h2>
 * <p>
 * If any of these fails, then the log in fails.
 * <ul>
 * <li>Received credentials.username() exists as a user</li>
 * <li>Received password matchs the one encrypted for the user</li>
 * <li>User should be enabled, and valid</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class SpringSecurityValidLoginPredicate implements Predicate<Credentials> {

    /**
     * Logger for the class.
     */
    private static final Logger      log = LoggerFactory.getLogger(SpringSecurityValidLoginPredicate.class);

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder    passwordEncoder;

    /**
     * User details service, to find and validate users.
     */
    private final UserDetailsService userDetailsService;

    public SpringSecurityValidLoginPredicate(final UserDetailsService userDetService,
            final PasswordEncoder passEncoder) {
        super();

        userDetailsService = Objects.requireNonNull(userDetService);
        passwordEncoder = Objects.requireNonNull(passEncoder);
    }

    @Override
    public final boolean test(final Credentials credentials) {
        final boolean         valid;
        Optional<UserDetails> details;
        final String          cleanedUsername;

        cleanedUsername = credentials.username()
            .toLowerCase(Locale.ROOT);

        // TODO: Throw exceptions
        log.debug("Validating login for user {}", cleanedUsername);

        // Find the user
        try {
            details = Optional.ofNullable(userDetailsService.loadUserByUsername(cleanedUsername));
        } catch (final UsernameNotFoundException e) {
            details = Optional.empty();
        }

        if (details.isEmpty()) {
            // No user found for username
            log.debug("No user for username {}. Failed login", cleanedUsername);
            valid = false;
        } else if (isValid(details.get())) {
            // User exists
            // Validate password
            log.debug("User {} exists, validating password", cleanedUsername);
            valid = passwordEncoder.matches(credentials.password(), details.get()
                .getPassword());
            if (!valid) {
                log.debug("Received a password which doesn't match the one stored for username {}. Failed login",
                    cleanedUsername);
            } else {
                log.debug("Received valid password for user {}", cleanedUsername);
            }
        } else {
            // Invalid user
            log.debug("User {} is in an invalid state. Failed login", cleanedUsername);
            if (!details.get()
                .isAccountNonExpired()) {
                log.debug("User {} account expired", cleanedUsername);
            }
            if (!details.get()
                .isAccountNonLocked()) {
                log.debug("User {} account is locked", cleanedUsername);
            }
            if (!details.get()
                .isCredentialsNonExpired()) {
                log.debug("User {} credentials expired", cleanedUsername);
            }
            if (!details.get()
                .isEnabled()) {
                log.debug("User {} is disabled", cleanedUsername);
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
