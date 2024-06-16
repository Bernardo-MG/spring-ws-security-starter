
package com.bernardomg.security.authorization.token.usecase.validation;

import java.util.Optional;

import com.bernardomg.security.authorization.token.domain.model.UserTokenPatch;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user token is not revoked.
 */
@Slf4j
public final class UserTokenNotRevokedRule implements FieldRule<UserTokenPatch> {

    public UserTokenNotRevokedRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final UserTokenPatch token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        // TODO: what if the token is already valid?
        if ((token.getRevoked() != null) && (!token.getRevoked())) {
            log.error("Reverting token revocation");
            fieldFailure = FieldFailure.of("revoked", "invalidValue", token.getRevoked());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
