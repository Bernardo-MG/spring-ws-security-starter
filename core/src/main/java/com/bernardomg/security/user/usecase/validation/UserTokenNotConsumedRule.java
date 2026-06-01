
package com.bernardomg.security.user.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.repository.UserTokenRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the user token is not consumed.
 */
public final class UserTokenNotConsumedRule implements FieldRule<UserToken> {

    /**
     * Logger for the class.
     */
    private static final Logger       log = LoggerFactory.getLogger(UserTokenNotConsumedRule.class);

    private final UserTokenRepository userTokenRepository;

    public UserTokenNotConsumedRule(final UserTokenRepository userTokenRepo) {
        super();

        userTokenRepository = Objects.requireNonNull(userTokenRepo);
    }

    @Override
    public final Optional<FieldFailure> check(final UserToken token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final UserToken              existing;

        existing = userTokenRepository.findOne(token.token())
            .get();
        if (existing.consumed()) {
            log.error("Editing consumed token");
            fieldFailure = new FieldFailure("invalidValue", "consumed", existing.consumed());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
