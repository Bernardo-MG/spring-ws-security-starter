
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
 * Checks the user token is not revoked.
 */
public final class UserTokenNotRevokedRule implements FieldRule<UserToken> {

    /**
     * Logger for the class.
     */
    private static final Logger       log = LoggerFactory.getLogger(UserTokenNotRevokedRule.class);

    private final UserTokenRepository userTokenRepository;

    public UserTokenNotRevokedRule(final UserTokenRepository userTokenRepo) {
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
        if (existing.revoked()) {
            log.error("Editing revoked token");
            fieldFailure = new FieldFailure("invalidValue", "revoked", existing.revoked());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
