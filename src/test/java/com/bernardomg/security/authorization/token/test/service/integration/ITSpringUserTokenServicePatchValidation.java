
package com.bernardomg.security.authorization.token.test.service.integration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authorization.token.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.service.SpringUserTokenService;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenPartials;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@DisplayName("SpringUserTokenService - patch validation")
class ITSpringUserTokenServicePatchValidation {

    @Autowired
    private SpringUserTokenService service;

    @Test
    @DisplayName("When trying to set the expiration date before the current date an exception is thrown")
    @OnlyUser
    @ValidUserToken
    void testUpdate_ExpireBeforeNow() {
        final ThrowingCallable execution;
        final FieldFailure     failure;
        final UserTokenPartial request;

        request = UserTokenPartials.expirationDateYesterday();

        execution = () -> service.patch(UserTokens.TOKEN, request);

        failure = FieldFailure.of("expirationDate.beforeToday", "expirationDate", "beforeToday",
            UserTokenPartials.DATE_YESTERDAY);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("When trying to enable a revoked token an exception is thrown")
    @OnlyUser
    @RevokedUserToken
    void testUpdate_RemoveRevoked() {
        final ThrowingCallable execution;
        final FieldFailure     failure;
        final UserTokenPartial request;

        request = UserTokenPartials.notRevoked();

        execution = () -> service.patch(UserTokens.TOKEN, request);

        failure = FieldFailure.of("revoked.invalidValue", "revoked", "invalidValue", false);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

}
