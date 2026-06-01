
package com.bernardomg.security.user.usecase.validation;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the user token expiration date is not in the past.
 */
public final class UserTokenExpirationDateNotInPastRule implements FieldRule<UserToken> {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserTokenExpirationDateNotInPastRule.class);

    public UserTokenExpirationDateNotInPastRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final UserToken token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final Instant                today;

        today = Instant.now();
        if ((token.expirationDate() != null) && (token.expirationDate()
            .isBefore(today))) {
            log.error("Token expiration date {} expired when checked at {}", token.expirationDate(), today);
            fieldFailure = new FieldFailure("beforeToday", "expirationDate", token.expirationDate());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
