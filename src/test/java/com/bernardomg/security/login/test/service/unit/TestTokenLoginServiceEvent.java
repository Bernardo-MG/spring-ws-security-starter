
package com.bernardomg.security.login.test.service.unit;

import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.assertj.core.api.Assertions;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.login.domain.event.LogInEvent;
import com.bernardomg.security.login.usecase.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.TokenLoginService;
import com.bernardomg.security.login.usecase.service.springframework.SpringValidLoginPredicate;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - login event handling")
class TestTokenLoginServiceEvent {

    @Captor
    private ArgumentCaptor<LogInEvent>         emailCaptor;

    @Mock
    private ApplicationEventPublisher          eventPublisher;

    @Mock
    private PasswordEncoder                    passEncoder;

    @Mock
    private ResourcePermissionSpringRepository resourcePermissionRepository;

    @Mock
    private TokenEncoder                       tokenEncoder;

    @Mock
    private UserDetailsService                 userDetService;

    @Mock
    private UserSpringRepository               userRepository;

    public TestTokenLoginServiceEvent() {
        super();
    }

    private final TokenLoginService getService(final UserDetails user) {
        final BiPredicate<String, String> valid;
        final LoginTokenEncoder           loginTokenEncoder;

        given(userDetService.loadUserByUsername(ArgumentMatchers.anyString())).willReturn(user);

        valid = new SpringValidLoginPredicate(userDetService, passEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, resourcePermissionRepository,
            Duration.ZERO);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventPublisher);
    }

    private final TokenLoginService getServiceForAccountExpired() {
        final UserDetails user;

        user = new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, false, true, true,
            Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForCredentialsExpired() {
        final UserDetails user;

        user = new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, true, false, true,
            Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForDisabled() {
        final UserDetails user;

        user = new User(UserConstants.USERNAME, UserConstants.PASSWORD, false, true, true, true,
            Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForLocked() {
        final UserDetails user;

        user = new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, true, false, true,
            Collections.emptyList());

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

        user = new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, true, true, true,
            Collections.emptyList());

        return getService(user);
    }

    private final void loadUser() {
        final UserEntity persistentUser;

        persistentUser = new UserEntity();
        persistentUser.setId(1l);
        persistentUser.setUsername(UserConstants.USERNAME);
        persistentUser.setPassword(UserConstants.EMAIL);
        given(userRepository.findOneByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(persistentUser));
    }

    @Test
    @DisplayName("With an expired account and logging with email it generates an event not logged in")
    void testLogIn_Email_AccountExpired() {
        final LogInEvent event;

        loadUser();

        getServiceForAccountExpired().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With expired credentials and logging with email it generates an event not logged in")
    void testLogIn_Email_CredentialsExpired() {
        final LogInEvent event;

        loadUser();

        getServiceForCredentialsExpired().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a disabled account and logging with email it generates an event not logged in")
    void testLogIn_Email_Disabled() {
        final LogInEvent event;

        loadUser();

        getServiceForDisabled().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a locked account and logging with email it generates an event not logged in")
    void testLogIn_Email_Locked() {
        final LogInEvent event;

        loadUser();

        getServiceForLocked().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a not existing user and logging with email it generates an event not logged in")
    void testLogIn_Email_NotExisting() {
        final LogInEvent event;

        getServiceForNotExisting().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.EMAIL);
    }

    @Test
    @DisplayName("With a valid account and logging with email it generates an event logged in")
    void testLogIn_Email_Valid() {
        final LogInEvent event;

        loadUser();

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        getServiceForValid().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isTrue();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With an expired account and logging with username it generates an event not logged in")
    void testLogIn_Username_AccountExpired() {
        final LogInEvent event;

        getServiceForAccountExpired().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With expired credentials and logging with username it generates an event not logged in")
    void testLogIn_Username_CredentialsExpired() {
        final LogInEvent event;

        getServiceForCredentialsExpired().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a disabled account and logging with username it generates an event not logged in")
    void testLogIn_Username_Disabled() {
        final LogInEvent event;

        getServiceForDisabled().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a locked account and logging with username it generates an event not logged in")
    void testLogIn_Username_Locked() {
        final LogInEvent event;

        getServiceForLocked().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a not existing user and logging with email it generates an event not logged in")
    void testLogIn_Username_NotExisting() {
        final LogInEvent event;

        getServiceForNotExisting().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a valid account and logging with username it generates an event logged in")
    void testLogIn_Username_Valid() {
        final LogInEvent event;

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        getServiceForValid().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Mockito.verify(eventPublisher)
            .publishEvent(emailCaptor.capture());

        event = emailCaptor.getValue();
        Assertions.assertThat(event.isLoggedIn())
            .as("logged in")
            .isTrue();
        Assertions.assertThat(event.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);
    }

}
