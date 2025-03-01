
package com.bernardomg.security.user.token.usecase.validation;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user token is not expired.
 */
@Slf4j
public final class UserTokenNotExpiredRule implements FieldRule<UserToken> {

    public UserTokenNotExpiredRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final UserToken token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final LocalDateTime          today;

        today = LocalDateTime.now()
            .minusMinutes(2);
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
