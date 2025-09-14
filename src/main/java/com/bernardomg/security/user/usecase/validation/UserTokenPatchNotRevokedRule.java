
package com.bernardomg.security.user.usecase.validation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the user token is not revoked.
 */
public final class UserTokenPatchNotRevokedRule implements FieldRule<UserToken> {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserTokenPatchNotRevokedRule.class);

    public UserTokenPatchNotRevokedRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final UserToken token) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        // TODO: what if the token is already valid?
        if ((token.revoked() != null) && (!token.revoked())) {
            log.error("Reverting token revocation");
            fieldFailure = new FieldFailure("invalidValue", "revoked", token.revoked());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
