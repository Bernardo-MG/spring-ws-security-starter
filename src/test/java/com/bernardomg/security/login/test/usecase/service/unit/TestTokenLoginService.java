
package com.bernardomg.security.login.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import java.util.function.BiPredicate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.login.usecase.encoder.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.TokenLoginService;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService")
class TestTokenLoginService {

    @Mock
    private ApplicationEventPublisher   eventPublisher;

    @Mock
    private LoginTokenEncoder           loginTokenEncoder;

    @InjectMocks
    private TokenLoginService           service;

    @Mock
    private UserRepository              userRepository;

    @Mock
    private BiPredicate<String, String> valid;

    public TestTokenLoginService() {
        super();
    }

    @Test
    @DisplayName("Doesn't log in using the email and with an invalid user")
    void testLogIn_Email_Invalid() {
        final TokenLoginStatus status;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(false);

        // WHEN
        status = service.login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a no existing user using the email")
    void testLogIn_Email_NotExisting() {
        final TokenLoginStatus status;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.empty());

        given(valid.test(UserConstants.EMAIL, UserConstants.PASSWORD)).willReturn(false);

        // WHEN
        status = service.login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in using the email and with a valid user")
    void testLogIn_Email_Valid() {
        final TokenLoginStatus status;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(true);

        // WHEN
        status = service.login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status.isLogged())
            .isTrue();
    }

    @Test
    @DisplayName("Doesn't log in using the username and with an invalid user")
    void testLogIn_Username_Invalid() {
        final TokenLoginStatus status;

        // GIVEN
        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(false);

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in using the username and with a valid user")
    void testLogIn_Username_Valid() {
        final TokenLoginStatus status;

        // GIVEN
        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(true);

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status.isLogged())
            .isTrue();
    }

}
