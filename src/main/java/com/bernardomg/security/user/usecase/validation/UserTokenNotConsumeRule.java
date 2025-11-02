
package com.bernardomg.security.user.usecase.validation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the user token is not set to consumed.
 */
public final class UserTokenNotConsumeRule implements FieldRule<UserToken> {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserTokenNotConsumeRule.class);

    public UserTokenNotConsumeRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final UserToken token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (Boolean.TRUE.equals(token.consumed())) {
            log.error("Editing consumed token");
            fieldFailure = new FieldFailure("invalidValue", "consumed", token.consumed());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
