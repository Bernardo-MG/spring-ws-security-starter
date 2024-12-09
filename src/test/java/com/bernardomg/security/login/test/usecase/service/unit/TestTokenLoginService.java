
package com.bernardomg.security.login.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import java.util.function.Predicate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.login.domain.model.Credentials;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.login.usecase.encoder.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.TokenLoginService;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService")
class TestTokenLoginService {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private LoginTokenEncoder         loginTokenEncoder;

    @Mock
    private UserRepository            userRepository;

    @Mock
    private Predicate<Credentials>    valid;

    public TestTokenLoginService() {
        super();
    }

    private final TokenLoginService getService(final Boolean passwordMatches) {
        // TODO: use constants
        given(valid.test(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD))).willReturn(passwordMatches);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventPublisher);
    }

    @Test
    @DisplayName("Doesn't log in using the email and with an invalid password")
    void testLogIn_Email_InvalidPassword() {
        final TokenLoginStatus status;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        status = getService(false).login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

        // THEN
        Assertions.assertThat(status.logged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in using the email and with a valid password")
    void testLogIn_Email_ValidPassword() {
        final TokenLoginStatus status;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        status = getService(true).login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

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
