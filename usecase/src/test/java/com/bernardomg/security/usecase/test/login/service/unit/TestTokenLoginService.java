
package com.bernardomg.security.usecase.test.login.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import java.util.function.Predicate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.domain.login.model.TokenLoginStatus;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.login.encoder.LoginTokenEncoder;
import com.bernardomg.security.usecase.login.service.TokenLoginService;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService")
class TestTokenLoginService {

    @Mock
    private EventEmitter           eventEmitter;

    @Mock
    private LoginTokenEncoder      loginTokenEncoder;

    @Mock
    private UserRepository         userRepository;

    @Mock
    private Predicate<Credentials> valid;

    public TestTokenLoginService() {
        super();
    }

    private final TokenLoginService getService(final Boolean passwordMatches) {
        // TODO: use constants
        given(valid.test(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD))).willReturn(passwordMatches);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventEmitter);
    }

    @Test
    @DisplayName("Doesn't log in using the email and with an invalid password")
    void testLogIn_Email_InvalidPassword() {
        final TokenLoginStatus status;

        // WHEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        status = getService(false).login(new Credentials(UserConstants.EMAIL, UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in using a padded email")
    void testLogIn_Email_Padded() {
        final TokenLoginStatus status;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = getService(true).login(new Credentials(" " + UserConstants.EMAIL + " ", UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isTrue();
    }

    @Test
    @DisplayName("Logs in using the email and with a valid password")
    void testLogIn_Email_ValidPassword() {
        final TokenLoginStatus status;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = getService(true).login(new Credentials(UserConstants.EMAIL, UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isTrue();
    }

    @Test
    @DisplayName("Logs in using a padded password")
    void testLogIn_PaddedPassword() {
        final TokenLoginStatus status;

        // GIVEN
        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = getService(true).login(new Credentials(" " + UserConstants.USERNAME + " ", UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isTrue();
    }

    @Test
    @DisplayName("Doesn't log in using the username and with an invalid password")
    void testLogIn_Username_InvalidPassword() {
        final TokenLoginStatus status;

        // WHEN
        status = getService(false).login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in using a padded username")
    void testLogIn_Username_Padded() {
        final TokenLoginStatus status;

        // GIVEN
        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = getService(true).login(new Credentials(" " + UserConstants.USERNAME + " ", UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isTrue();
    }

    @Test
    @DisplayName("Logs in using the username and with a valid password")
    void testLogIn_Username_ValidPassword() {
        final TokenLoginStatus status;

        // GIVEN
        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = getService(true).login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isTrue();
    }

}
