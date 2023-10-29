
package com.bernardomg.security.authorization.permission.validation;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * User validator which checks if the user for a username is active with the help if {@link UserDetailsService}.
 * <h2>Validations</h2>
 * <p>
 * If any of these fails, then the username is invalid.
 * <ul>
 * <li>User should be enabled</li>
 * <li>User should not be locked</li>
 * <li>User should not be expired</li>
 * <li>User should not have the credentials expired</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class UserDetailsServiceUserValidPredicate implements Predicate<String> {

    private final UserDetailsService userDetailsService;

    public UserDetailsServiceUserValidPredicate(@NonNull final UserDetailsService userDetService) {
        super();

        userDetailsService = userDetService;
    }

    @Override
    public final boolean test(final String username) {
        final Boolean         valid;
        Optional<UserDetails> details;

        // Find the user
        try {
            details = Optional
                .ofNullable(userDetailsService.loadUserByUsername(username.toLowerCase(Locale.getDefault())));
        } catch (final UsernameNotFoundException e) {
            details = Optional.empty();
        }

        if (details.isEmpty()) {
            // No user found for username
            log.debug("No user for username {}", username);
            valid = false;
        } else if (isValid(details.get())) {
            // User exists
            valid = true;
        } else {
            // Invalid user
            log.debug("Username {} is in an invalid state", username);
            if (!details.get()
                .isAccountNonExpired()) {
                log.debug("User {} expired", username);
            }
            if (!details.get()
                .isAccountNonLocked()) {
                log.debug("User {} is locked", username);
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
     * @param user
     *            user to check
     * @return {@code true} if the user is valid, {@code false} otherwise
     */
    private final boolean isValid(final UserDetails user) {
        return (user.isAccountNonExpired()) && (user.isAccountNonLocked()) && (user.isCredentialsNonExpired())
                && (user.isEnabled());
    }

}
