
package com.bernardomg.security.user.token.usecase.validation;

import java.util.Optional;

import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user token is not revoked.
 */
@Slf4j
public final class UserTokenNotRevokedRule implements FieldRule<UserToken> {

    public UserTokenNotRevokedRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final UserToken token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        // TODO: what if the token is already valid?
        if (!token.isRevoked()) {
            log.error("Reverting token revocation");
            fieldFailure = FieldFailure.of("revoked", "invalidValue", token.isRevoked());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
