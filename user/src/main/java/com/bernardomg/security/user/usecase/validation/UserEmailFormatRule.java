
package com.bernardomg.security.user.usecase.validation;

import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the email has a valid format.
 */
public final class UserEmailFormatRule implements FieldRule<User> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
        Pattern.CASE_INSENSITIVE);

    /**
     * Logger for the class.
     */
    private static final Logger  log           = LoggerFactory.getLogger(UserEmailFormatRule.class);

    public UserEmailFormatRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final User user) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final boolean                invalidEmail;

        invalidEmail = !EMAIL_PATTERN.matcher(user.email())
            .matches();
        if (invalidEmail) {
            log.error("The email {} has an invalid format", user.email());
            fieldFailure = new FieldFailure("invalid", "email", user.email());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
