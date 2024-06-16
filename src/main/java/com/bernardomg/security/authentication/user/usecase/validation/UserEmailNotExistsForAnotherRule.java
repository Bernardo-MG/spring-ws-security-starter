
package com.bernardomg.security.authentication.user.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user's email is not registered for another user. This particular condition is required for validating
 * updates, otherwise the user will reject its own email.
 */
@Slf4j
public final class UserEmailNotExistsForAnotherRule implements FieldRule<User> {

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    public UserEmailNotExistsForAnotherRule(final UserRepository userRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
    }

    @Override
    public final Optional<FieldFailure> check(final User user) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (userRepository.existsEmailForAnotherUser(user.getUsername(), user.getEmail())) {
            log.error("Another user distinct to {} already exists with the email {}", user.getUsername(),
                user.getEmail());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            fieldFailure = FieldFailure.of("email", "existing", user.getEmail());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
