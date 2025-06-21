
package com.bernardomg.security.user.data.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the user's email is not registered.
 */
public final class UserEmailNotExistsRule implements FieldRule<User> {

    /**
     * Logger for the class.
     */
    private static final Logger  log = LoggerFactory.getLogger(UserEmailNotExistsRule.class);

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
