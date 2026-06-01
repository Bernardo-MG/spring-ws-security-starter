/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.domain.exception.ConsumedTokenException;
import com.bernardomg.security.user.domain.model.UserTokenStatus;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.usecase.service.DefaultUserOnboardingService;
import com.bernardomg.security.user.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserActivationService - token validation")
class TestUserOnboardingServiceValidateToken {

    @Mock
    private EventEmitter                 eventEmitter;

    @Mock
    private PasswordEncoder              passwordEncoder;

    @Mock
    private UserRepository               repository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultUserOnboardingService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Test
    void testValidateToken_Invalid() {
        final UserTokenStatus status;

        // GIVEN
        willThrow(ConsumedTokenException.class).given(tokenStore)
            .validate(Tokens.TOKEN);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        status = service.validateToken(Tokens.TOKEN);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.valid())
                .isFalse();
            softly.assertThat(status.username())
                .isEqualTo(UserConstants.USERNAME);
        });
    }

    @Test
    void testValidateToken_Valid() {
        final UserTokenStatus status;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        status = service.validateToken(Tokens.TOKEN);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.valid())
                .isTrue();
            softly.assertThat(status.username())
                .isEqualTo(UserConstants.USERNAME);
        });
    }

}
