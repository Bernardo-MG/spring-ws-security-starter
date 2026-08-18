
package com.bernardomg.security.usecase.test.password.reset.service.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.user.exception.InvalidTokenException;
import com.bernardomg.security.domain.user.model.UserTokenStatus;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.password.reset.service.DefaultPasswordResetService;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokenConstants;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokenStatuses;
import com.bernardomg.security.usecase.token.TokenValidator;
import com.bernardomg.security.usecase.token.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("Password reset service - validate token")
class TestPasswordResetServiceValidateToken {

    @Mock
    private EventEmitter                eventEmitter;

    @Mock
    private InvalidTokenException       invalidTokenException;

    @Mock
    private PasswordEncrypter           passwordEncrypter;

    @InjectMocks
    private DefaultPasswordResetService service;

    @Mock
    private UserTokenStore              tokenStore;

    @Mock
    private TokenValidator              tokenValidator;

    @Mock
    private UserRepository              userRepository;

    public TestPasswordResetServiceValidateToken() {
        super();
    }

    @Test
    @DisplayName("When the token is valid its username and valid status are returned")
    void testValidateToken() {
        final UserTokenStatus status;

        // GIVEN
        given(tokenStore.getUsername(UserTokenConstants.TOKEN)).willReturn(UserTokenConstants.USERNAME);

        // WHEN
        status = service.validateToken(UserTokenConstants.TOKEN);

        // THEN
        assertThat(status).as("token status")
            .isEqualTo(UserTokenStatuses.valid());

        then(tokenValidator).should()
            .validate(UserTokenConstants.TOKEN);
        then(tokenStore).should()
            .getUsername(UserTokenConstants.TOKEN);
    }

    @Test
    @DisplayName("When validation fails the username and invalid status are returned")
    void testValidateToken_Invalid() {
        final UserTokenStatus status;

        // GIVEN
        willThrow(invalidTokenException).given(tokenValidator)
            .validate(UserTokenConstants.TOKEN);
        given(tokenStore.getUsername(UserTokenConstants.TOKEN)).willReturn(UserTokenConstants.USERNAME);

        // WHEN
        status = service.validateToken(UserTokenConstants.TOKEN);

        // THEN
        assertThat(status).as("token status")
            .isEqualTo(UserTokenStatuses.invalid());
    }

    @Test
    @DisplayName("When validation and username retrieval fail an empty invalid status is returned")
    void testValidateToken_InvalidAndMissingUsername() {
        final UserTokenStatus status;

        // GIVEN
        willThrow(invalidTokenException).given(tokenValidator)
            .validate(UserTokenConstants.TOKEN);
        given(tokenStore.getUsername(UserTokenConstants.TOKEN)).willThrow(invalidTokenException);

        // WHEN
        status = service.validateToken(UserTokenConstants.TOKEN);

        // THEN
        assertThat(status).as("token status")
            .isEqualTo(UserTokenStatuses.invalidMissingUsername());
    }

    @Test
    @DisplayName("When the username can't be obtained an empty username is returned")
    void testValidateToken_MissingUsername() {
        final UserTokenStatus status;

        // GIVEN
        given(tokenStore.getUsername(UserTokenConstants.TOKEN)).willThrow(invalidTokenException);

        // WHEN
        status = service.validateToken(UserTokenConstants.TOKEN);

        // THEN
        assertThat(status).as("token status")
            .isEqualTo(UserTokenStatuses.validMissingUsername());
    }

}
