
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
import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.domain.login.model.TokenLoginStatus;
import com.bernardomg.security.usecase.login.authentication.UserAuthenticator;
import com.bernardomg.security.usecase.login.encoder.LoginTokenEncoder;
import com.bernardomg.security.usecase.login.service.TokenLoginService;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;

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
    @DisplayName("Doesn't log in using the email and with invalid credentials")
    void testLogIn_Email_InvalidCredentials() {
        final TokenLoginStatus status;

        // GIVEN
        given(userAuthenticator.load(UserConstants.EMAIL, UserConstants.PASSWORD))
            .willThrow(new InvalidCredentialsException());

        // WHEN
        status = service.login(new Credentials(UserConstants.EMAIL, UserConstants.PASSWORD));

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.logged())
                .isFalse();
            softly.assertThat(status.token())
                .isEmpty();
        });
    }

    @Test
    @DisplayName("Logs in using the email and with valid credentials")
    void testLogIn_Email_ValidCredentials() {
        final TokenLoginStatus status;

        // GIVEN
        given(userAuthenticator.load(UserConstants.EMAIL, UserConstants.PASSWORD)).willReturn(Users.enabled());

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = service.login(new Credentials(UserConstants.EMAIL, UserConstants.PASSWORD));

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
    void testLogIn_Username_InvalidCredentials() {
        final TokenLoginStatus status;

        // GIVEN
        given(userAuthenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD))
            .willThrow(new InvalidCredentialsException());

        // WHEN
        status = service.login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isFalse();

        Assertions.assertThat(status.token())
            .isEmpty();
    }

    @Test
    @DisplayName("Logs in using a padded username")
    void testLogIn_Username_Padded() {
        final TokenLoginStatus status;
        final String           paddedUsername;

        // GIVEN
        paddedUsername = " " + UserConstants.USERNAME + " ";

        given(userAuthenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(Users.enabled());

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = service.login(new Credentials(paddedUsername, UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isTrue();
    }

    @Test
    @DisplayName("Logs in using the username and with valid credentials")
    void testLogIn_Username_ValidCredentials() {
        final TokenLoginStatus status;

        // GIVEN
        given(userAuthenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(Users.enabled());

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = service.login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

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

}
