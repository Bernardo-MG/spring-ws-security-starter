
package com.bernardomg.security.login.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.login.adapter.inbound.spring.SpringValidLoginPredicate;
import com.bernardomg.security.login.domain.event.LogInEvent;
import com.bernardomg.security.login.usecase.encoder.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.usecase.encoder.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.TokenLoginService;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - login event handling")
class TestTokenLoginServiceEvent {

    @Captor
    private ArgumentCaptor<LogInEvent>   emailCaptor;

    @Mock
    private ApplicationEventPublisher    eventPublisher;

    @Mock
    private PasswordEncoder              passEncoder;

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private TokenEncoder                 tokenEncoder;

    @Mock
    private UserDetailsService           userDetService;

    @Mock
    private UserRepository               userRepository;

    public TestTokenLoginServiceEvent() {
        super();
    }

    private final TokenLoginService getService(final UserDetails user) {
        final BiPredicate<String, String> valid;
        final LoginTokenEncoder           loginTokenEncoder;

        given(userDetService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);

        // TODO: Mock this
        valid = new SpringValidLoginPredicate(userDetService, passEncoder);

        // TODO: Mock this
        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, resourcePermissionRepository,
            Duration.ZERO);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventPublisher);
    }

    private final TokenLoginService getServiceForAccountExpired() {
        final UserDetails user;

        user = new org.springframework.security.core.userdetails.User(UserConstants.USERNAME,
            UserConstants.ENCODED_PASSWORD, true, false, true, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForCredentialsExpired() {
        final UserDetails user;

        user = new org.springframework.security.core.userdetails.User(UserConstants.USERNAME,
            UserConstants.ENCODED_PASSWORD, true, true, false, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForDisabled() {
        final UserDetails user;

        user = new org.springframework.security.core.userdetails.User(UserConstants.USERNAME,
            UserConstants.ENCODED_PASSWORD, false, true, true, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForLocked() {
        final UserDetails user;

        user = new org.springframework.security.core.userdetails.User(UserConstants.USERNAME,
            UserConstants.ENCODED_PASSWORD, true, true, false, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForNotExisting() {
        final BiPredicate<String, String> valid;
        final LoginTokenEncoder           loginTokenEncoder;

        given(userDetService.loadUserByUsername(ArgumentMatchers.anyString()))
            .willThrow(UsernameNotFoundException.class);

        valid = new SpringValidLoginPredicate(userDetService, passEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, resourcePermissionRepository,
            Duration.ZERO);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventPublisher);
    }

    private final TokenLoginService getServiceForValid() {
        final UserDetails user;

        user = new org.springframework.security.core.userdetails.User(UserConstants.USERNAME,
            UserConstants.ENCODED_PASSWORD, true, true, true, true, Collections.emptyList());

        return getService(user);
    }

    private final void loadUser() {
        final User user;

        user = Users.enabled();
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(user));
    }

    @Test
    @DisplayName("With an expired account and logging with email it generates an event not logged in")
    void testLogIn_Email_AccountExpired() {
        final LogInEvent event;

        // GIVEN
        loadUser();

        // WHEN
        getServiceForAccountExpired().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();

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
    @DisplayName("With expired credentials and logging with email it generates an event not logged in")
    void testLogIn_Email_CredentialsExpired() {
        final LogInEvent event;

        // GIVEN
        loadUser();

        // WHEN
        getServiceForCredentialsExpired().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
    @DisplayName("With a disabled account and logging with email it generates an event not logged in")
    void testLogIn_Email_Disabled() {
        final LogInEvent event;

        // GIVEN
        loadUser();

        // WHEN
        getServiceForDisabled().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
    @DisplayName("With a locked account and logging with email it generates an event not logged in")
    void testLogIn_Email_Locked() {
        final LogInEvent event;

        // GIVEN
        loadUser();

        // WHEN
        getServiceForLocked().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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

        // WHEN
        getServiceForNotExisting().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
        loadUser();

        given(passEncoder.matches(UserConstants.PASSWORD, UserConstants.ENCODED_PASSWORD)).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        // WHEN
        getServiceForValid().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
    void testLogIn_Username_AccountExpired() {
        final LogInEvent event;

        // WHEN
        getServiceForAccountExpired().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
    @DisplayName("With expired credentials and logging with username it generates an event not logged in")
    void testLogIn_Username_CredentialsExpired() {
        final LogInEvent event;

        // WHEN
        getServiceForCredentialsExpired().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
    @DisplayName("With a disabled account and logging with username it generates an event not logged in")
    void testLogIn_Username_Disabled() {
        final LogInEvent event;

        // WHEN
        getServiceForDisabled().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
    @DisplayName("With a locked account and logging with username it generates an event not logged in")
    void testLogIn_Username_Locked() {
        final LogInEvent event;

        // WHEN
        getServiceForLocked().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
    void testLogIn_Username_NotExisting() {
        final LogInEvent event;

        // WHEN
        getServiceForNotExisting().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
        given(passEncoder.matches(UserConstants.PASSWORD, UserConstants.ENCODED_PASSWORD)).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        // WHEN
        getServiceForValid().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
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
