
package com.bernardomg.security.token.usecase.validation;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bernardomg.security.token.domain.model.UserTokenPatch;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user token is not expired.
 */
@Slf4j
public final class UserTokenNotExpiredRule implements FieldRule<UserTokenPatch> {

    public UserTokenNotExpiredRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final UserTokenPatch token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final LocalDateTime          today;

        today = LocalDateTime.now()
            .minusMinutes(2);
        if ((token.getExpirationDate() != null) && (token.getExpirationDate()
            .isBefore(today))) {
            log.error("Token expiration date {} expired when checked at {}", token.getExpirationDate(), today);
            fieldFailure = FieldFailure.of("expirationDate", "beforeToday", token.getExpirationDate());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
