
package com.bernardomg.security.usecase.test.user.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.domain.user.exception.ConsumedTokenException;
import com.bernardomg.security.domain.user.model.UserTokenStatus;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.token.TokenValidator;
import com.bernardomg.security.usecase.token.UserTokenStore;
import com.bernardomg.security.usecase.user.service.DefaultUserOnboardingService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserOnboardingService - validate token")
class TestUserOnboardingServiceValidateToken {

    @Mock
    private EventEmitter                 eventEmitter;

    @Mock
    private PasswordEncrypter            passwordEncrypt;

    @Mock
    private UserRepository               repository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultUserOnboardingService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Mock
    private TokenValidator               tokenValidator;

    @Test
    @DisplayName("When token validation fails, then an invalid status and the username are returned")
    void testValidateToken_Invalid() {
        final UserTokenStatus status;

        // GIVEN
        willThrow(ConsumedTokenException.class).given(tokenValidator)
            .validate(Tokens.TOKEN);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        status = service.validateToken(Tokens.TOKEN);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.valid())
                .as("When token validation fails, then an invalid status and the username are returned (status valid)")
                .isFalse();
            softly.assertThat(status.username())
                .as("When token validation fails, then an invalid status and the username are returned (status username)")
                .isEqualTo(UserConstants.USERNAME);
        });
    }

    @Test
    @DisplayName("When token validation succeeds, then a valid status and the username are returned")
    void testValidateToken_Valid() {
        final UserTokenStatus status;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        status = service.validateToken(Tokens.TOKEN);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.valid())
                .as("When token validation succeeds, then a valid status and the username are returned (status valid)")
                .isTrue();
            softly.assertThat(status.username())
                .as("When token validation succeeds, then a valid status and the username are returned (status username)")
                .isEqualTo(UserConstants.USERNAME);
        });
    }

}
