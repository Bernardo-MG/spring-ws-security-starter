
package com.bernardomg.security.authentication.user.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;

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

        // Get number of attempts
        attempts = userRepository.getLoginAttempts(username);
        // If attempts + 1 >= max
        if ((attempts + 1) >= maxAttempts) {
            // Then lock user
            read = userRepository.findOne(username);
            if (read.isEmpty()) {
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
        }
    }

    @Override
    public final void clearLoginAttempts(final String username) {
        // Set attempts to 0
    }

}
