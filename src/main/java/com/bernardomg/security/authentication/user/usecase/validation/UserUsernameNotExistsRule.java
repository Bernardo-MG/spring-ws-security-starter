
package com.bernardomg.security.authentication.user.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user's username is not registered.
 */
@Slf4j
public final class UserUsernameNotExistsRule implements FieldRule<User> {

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    public UserUsernameNotExistsRule(final UserRepository userRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
    }

    @Override
    public final Optional<FieldFailure> check(final User user) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (userRepository.exists(user.getUsername())) {
            log.error("A user already exists with the username {}", user.getUsername());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            fieldFailure = FieldFailure.of("username", "existing", user.getUsername());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
