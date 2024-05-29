
package com.bernardomg.security.authentication.user.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
public final class DefaultUserAccessService implements UserAccessService {

    private final int            maxAttempts;

    private final UserRepository userRepository;

    public DefaultUserAccessService(final int maxAttmp, final UserRepository userRepo) {
        super();

        maxAttempts = Objects.requireNonNull(maxAttmp);
        userRepository = Objects.requireNonNull(userRepo);
    }

    @Override
    public final void checkForLocking(final String username) {
        final int            attempts;
        final Optional<User> read;
        final User           user;
        final User           locked;

        log.debug("Checking {} for locking user", username);

        // Get number of attempts
        attempts = userRepository.increaseLoginAttempts(username);

        log.debug("User {} had {} login attempts, out of a max of {}", username, attempts, maxAttempts);

        // If attempts reached the max
        if (attempts >= maxAttempts) {
            // Then the user is locked
            read = userRepository.findOne(username);
            if (read.isEmpty()) {
                log.error("Missing user {}", username);
                throw new MissingUserException(username);
            }
            user = read.get();
            locked = User.builder()
                .withUsername(user.getUsername())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .withEnabled(user.isEnabled())
                .withExpired(user.isExpired())
                .withLocked(true)
                .withPasswordExpired(user.isPasswordExpired())
                .withRoles(user.getRoles())
                .build();
            userRepository.update(locked);
            log.debug("Locked user {} after {} login attempts", username, attempts);
        }
    }

    @Override
    public final void clearLoginAttempts(final String username) {
        final int attempts;

        log.debug("Clearing login attempts for {}", username);

        attempts = userRepository.getLoginAttempts(username);
        if (attempts > 0) {
            log.debug("User {} had {} login attempts. Clearing them", username, attempts);
            userRepository.clearLoginAttempts(username);
        }
    }

}
