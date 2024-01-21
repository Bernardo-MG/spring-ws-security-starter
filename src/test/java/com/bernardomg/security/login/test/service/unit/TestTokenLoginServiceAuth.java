
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
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
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionRepository;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.login.usecase.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.TokenLoginService;
import com.bernardomg.security.login.usecase.service.springframework.SpringValidLoginPredicate;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - login with various user status")
class TestTokenLoginServiceAuth {

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
    private UserSpringRepository         userRepository;

    public TestTokenLoginServiceAuth() {
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
    @DisplayName("Doesn't log in using the email a expired user")
    void testLogIn_Email_AccountExpired() {
        final TokenLoginStatus status;

        loadUser();

        status = getServiceForAccountExpired().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a user with expired credentials")
    void testLogIn_Email_CredentialsExpired() {
        final TokenLoginStatus status;

        loadUser();

        status = getServiceForCredentialsExpired().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a disabled user")
    void testLogIn_Email_Disabled() {
        final TokenLoginStatus status;

        loadUser();

        status = getServiceForDisabled().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a locked user")
    void testLogIn_Email_Locked() {
        final TokenLoginStatus status;

        loadUser();

        status = getServiceForLocked().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a not existing user")
    void testLogIn_Email_NotExisting() {
        final TokenLoginStatus status;

        status = getServiceForNotExisting().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid email")
    void testLogIn_Email_Valid() {
        final TokenLoginStatus status;

        loadUser();

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        status = getServiceForValid().login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
    }

    @Test
    @DisplayName("Doesn't log in using the username a expired user")
    void testLogIn_Username_AccountExpired() {
        final TokenLoginStatus status;

        status = getServiceForAccountExpired().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a user with expired credentials")
    void testLogIn_Username_CredentialsExpired() {
        final TokenLoginStatus status;

        status = getServiceForCredentialsExpired().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a disabled user")
    void testLogIn_Username_Disabled() {
        final TokenLoginStatus status;

        status = getServiceForDisabled().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a locked user")
    void testLogIn_Username_Locked() {
        final TokenLoginStatus status;

        status = getServiceForLocked().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a not existing user")
    void testLogIn_Username_NotExisting() {
        final TokenLoginStatus status;

        status = getServiceForNotExisting().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid username")
    void testLogIn_Username_Valid() {
        final TokenLoginStatus status;

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        status = getServiceForValid().login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
    }

}
