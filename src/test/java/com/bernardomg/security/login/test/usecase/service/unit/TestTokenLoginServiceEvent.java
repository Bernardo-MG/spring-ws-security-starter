
package com.bernardomg.security.login.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import java.util.function.BiPredicate;

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
import org.springframework.context.ApplicationEventPublisher;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.event.LogInEvent;
import com.bernardomg.security.login.usecase.encoder.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.TokenLoginService;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - login event handling")
class TestTokenLoginServiceEvent {

    @Captor
    private ArgumentCaptor<LogInEvent>  eventCaptor;

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

    public TestTokenLoginServiceEvent() {
        super();
    }

    @Test
    @DisplayName("With an expired account and logging with email it generates an event not logged in")
    void testLogIn_Email_Invalid() {
        final LogInEvent event;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(false);

        // WHEN
        service.login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(eventCaptor.capture());

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
    @DisplayName("With a not existing user and logging with email it generates an event not logged in")
    void testLogIn_Email_NotExisting() {
        final LogInEvent event;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.empty());

        // WHEN
        service.login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(eventCaptor.capture());

        event = eventCaptor.getValue();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(event.isLoggedIn())
                .as("logged in")
                .isFalse();
            softly.assertThat(event.getUsername())
                .as("username")
                .isEqualTo(UserConstants.EMAIL);
        });
    }

    @Test
    @DisplayName("With a valid account and logging with email it generates an event logged in")
    void testLogIn_Email_Valid() {
        final LogInEvent event;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));

        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(true);

        // WHEN
        service.login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(eventCaptor.capture());

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

    @Test
    @DisplayName("With an expired account and logging with username it generates an event not logged in")
    void testLogIn_Username_Invalid() {
        final LogInEvent event;

        // GIVEN
        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(false);

        // WHEN
        service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(eventCaptor.capture());

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
    @DisplayName("With a valid account and logging with username it generates an event logged in")
    void testLogIn_Username_Valid() {
        final LogInEvent event;

        // GIVEN
        given(loginTokenEncoder.encode(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        given(valid.test(UserConstants.USERNAME, UserConstants.PASSWORD)).willReturn(true);

        // WHEN
        service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(eventCaptor.capture());

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
