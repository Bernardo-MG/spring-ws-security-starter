
package com.bernardomg.security.user.data.usecase.validation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the email has a valid format.
 */
public final class UserNameNotEmptyRule implements FieldRule<User> {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserNameNotEmptyRule.class);

    public UserNameNotEmptyRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final User user) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (ObjectUtils.isEmpty(user.name())) {
            log.error("The user name is empty");
            fieldFailure = new FieldFailure("empty", "name", "");
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
