
package com.bernardomg.security.usecase.test.login.service.unit;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.login.exception.InvalidCredentialsException;
import com.bernardomg.security.domain.login.model.TokenLoginStatus;
import com.bernardomg.security.usecase.login.authentication.UserAuthenticator;
import com.bernardomg.security.usecase.login.encoder.LoginTokenEncoder;
import com.bernardomg.security.usecase.login.service.TokenLoginService;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.login.config.factory.Credentialses;
import com.bernardomg.security.usecase.test.user.config.factory.LoginUsers;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService")
class TestTokenLoginService {

    @Mock
    private EventEmitter      eventEmitter;

    @Mock
    private LoginTokenEncoder loginTokenEncoder;

    @InjectMocks
    private TokenLoginService service;

    @Mock
    private UserAuthenticator userAuthenticator;

    @Test
    @DisplayName("When loggin in the status and token is returned")
    void testLogIn() {
        final TokenLoginStatus status;

        // GIVEN
        given(userAuthenticator.authenticate(Credentialses.valid())).willReturn(LoginUsers.valid());

        given(loginTokenEncoder.encode(LoginUsers.valid())).willReturn(Tokens.TOKEN);

        // WHEN
        status = service.login(Credentialses.valid());

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.logged())
                .as("logged")
                .isTrue();
            softly.assertThat(status.token())
                .as("token")
                .isEqualTo(Tokens.TOKEN);
        });
    }

    @Test
    @DisplayName("Doesn't log in using the username and with invalid credentials")
    void testLogIn_InvalidCredentials() {
        final TokenLoginStatus status;

        // GIVEN
        given(userAuthenticator.authenticate(Credentialses.valid())).willThrow(new InvalidCredentialsException());

        // WHEN
        status = service.login(Credentialses.valid());

        // THEN
        Assertions.assertThat(status.logged())
            .isFalse();

        Assertions.assertThat(status.token())
            .isEmpty();
    }

}
