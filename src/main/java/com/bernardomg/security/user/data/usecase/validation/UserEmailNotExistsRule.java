
package com.bernardomg.security.user.data.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user's email is not registered.
 */
@Slf4j
public final class UserEmailNotExistsRule implements FieldRule<User> {

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    public UserEmailNotExistsRule(final UserRepository userRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
    }

    @Override
    public final Optional<FieldFailure> check(final User user) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (userRepository.existsByEmail(user.email())) {
            log.error("A user already exists with the email {}", user.email());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            fieldFailure = new FieldFailure("existing", "email", user.email());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
