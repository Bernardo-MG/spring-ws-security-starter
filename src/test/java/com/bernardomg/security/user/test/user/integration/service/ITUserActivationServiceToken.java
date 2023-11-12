/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.user.test.user.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.user.test.config.ValidUser;
import com.bernardomg.security.user.token.model.UserTokenStatus;
import com.bernardomg.security.user.token.test.config.annotation.UserRegisteredConsumedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.UserRegisteredExpiredUserToken;
import com.bernardomg.security.user.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.user.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - token validation")
class ITUserActivationServiceToken {

    @Autowired
    private UserActivationService service;

    public ITUserActivationServiceToken() {
        super();
        // TODO: Maybe should be a unit test
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("A consumed token is not valid")
    @ValidUser
    @UserRegisteredConsumedUserToken
    void testValidateToken_Consumed() {
        final UserTokenStatus status;

        status = service.validateToken(UserTokenConstants.TOKEN);

        Assertions.assertThat(status.isValid())
            .isFalse();
        Assertions.assertThat(status.getUsername())
            .isEqualTo("admin");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("An expired token is not valid")
    @ValidUser
    @UserRegisteredExpiredUserToken
    void testValidateToken_Expired() {
        final UserTokenStatus status;

        status = service.validateToken(UserTokenConstants.TOKEN);

        Assertions.assertThat(status.isValid())
            .isFalse();
        Assertions.assertThat(status.getUsername())
            .isEqualTo("admin");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("A valid token is valid")
    @ValidUser
    @UserRegisteredUserToken
    void testValidateToken_Valid() {
        final UserTokenStatus status;

        status = service.validateToken(UserTokenConstants.TOKEN);

        Assertions.assertThat(status.isValid())
            .isTrue();
        Assertions.assertThat(status.getUsername())
            .isEqualTo("admin");
    }

}
