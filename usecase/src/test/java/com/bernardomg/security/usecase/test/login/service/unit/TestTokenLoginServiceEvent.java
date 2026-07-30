
package com.bernardomg.security.usecase.test.login.service.unit;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.login.event.LogInEvent;
import com.bernardomg.security.domain.login.exception.InvalidCredentialsException;
import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.usecase.login.authentication.UserAuthenticator;
import com.bernardomg.security.usecase.login.encoder.LoginTokenEncoder;
import com.bernardomg.security.usecase.login.service.TokenLoginService;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.user.config.factory.LoginUsers;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - login event handling")
class TestTokenLoginServiceEvent {

    @Captor
    private ArgumentCaptor<LogInEvent> eventCaptor;

    @Mock
    private EventEmitter               eventEmitter;

    @Mock
    private LoginTokenEncoder          loginTokenEncoder;

    @InjectMocks
    private TokenLoginService          service;

    @Mock
    private UserAuthenticator          userAuthenticator;

    public TestTokenLoginServiceEvent() {
        super();
    }

    @Test
    @DisplayName("With an invalid user it generates an event not logged in")
    void testLogIn_Invalid() {
        final LogInEvent event;

        // GIVEN
        given(userAuthenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD))
            .willThrow(new InvalidCredentialsException());

        // WHEN
        service.login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

        // THEN
        Mockito.verify(eventEmitter)
            .emit(eventCaptor.capture());

        event = eventCaptor.getValue();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(event.isLoggedIn())
                .as("logged in")
                .isFalse();
            softly.assertThat(event.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
        });
    }

    @Test
    @DisplayName("With a valid account and logging with username it generates a logged in event")
    void testLogIn_Valid() {
        final LogInEvent event;

        // GIVEN
        given(userAuthenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(LoginUsers.valid());

        given(loginTokenEncoder.encode(LoginUsers.valid())).willReturn(Tokens.TOKEN);

        // WHEN
        service.login(new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD));

        // THEN
        Mockito.verify(eventEmitter)
            .emit(eventCaptor.capture());

        event = eventCaptor.getValue();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(event.isLoggedIn())
                .as("logged in")
                .isTrue();
            softly.assertThat(event.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
        });
    }

}
