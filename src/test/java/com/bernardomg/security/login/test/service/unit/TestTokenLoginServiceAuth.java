
package com.bernardomg.security.login.test.service.unit;

import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

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
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.permission.persistence.repository.UserGrantedPermissionRepository;
import com.bernardomg.security.login.model.LoginStatus;
import com.bernardomg.security.login.model.request.Login;
import com.bernardomg.security.login.model.request.LoginRequest;
import com.bernardomg.security.login.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.service.LoginTokenEncoder;
import com.bernardomg.security.login.service.TokenLoginService;
import com.bernardomg.security.login.service.springframework.SpringValidLoginPredicate;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - login with various user status")
class TestTokenLoginServiceAuth {

    @Mock
    private ApplicationEventPublisher       eventPublisher;

    @Mock
    private PasswordEncoder                 passEncoder;

    @Mock
    private TokenEncoder                    tokenEncoder;

    @Mock
    private UserDetailsService              userDetService;

    @Mock
    private UserGrantedPermissionRepository userGrantedPermissionRepository;

    @Mock
    private UserRepository                  userRepository;

    public TestTokenLoginServiceAuth() {
        super();
    }

    private final TokenLoginService getService(final UserDetails user) {
        final Predicate<Login>  valid;
        final LoginTokenEncoder loginTokenEncoder;

        given(userDetService.loadUserByUsername(ArgumentMatchers.anyString())).willReturn(user);

        valid = new SpringValidLoginPredicate(userDetService, passEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, userGrantedPermissionRepository,
            Duration.ZERO);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventPublisher);
    }

    private final TokenLoginService getServiceForAccountExpired() {
        final UserDetails user;

        user = new User("username", "password", true, false, true, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForCredentialsExpired() {
        final UserDetails user;

        user = new User("username", "password", true, true, false, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForDisabled() {
        final UserDetails user;

        user = new User("username", "password", false, true, true, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForLocked() {
        final UserDetails user;

        user = new User("username", "password", true, true, false, true, Collections.emptyList());

        return getService(user);
    }

    private final TokenLoginService getServiceForNotExisting() {
        final Predicate<Login>  valid;
        final LoginTokenEncoder loginTokenEncoder;

        given(userDetService.loadUserByUsername(ArgumentMatchers.anyString()))
            .willThrow(UsernameNotFoundException.class);

        valid = new SpringValidLoginPredicate(userDetService, passEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, userGrantedPermissionRepository,
            Duration.ZERO);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventPublisher);
    }

    private final TokenLoginService getServiceForValid() {
        final UserDetails user;

        user = new User("username", "password", true, true, true, true, Collections.emptyList());

        return getService(user);
    }

    private final void loadUser() {
        final UserEntity persistentUser;

        persistentUser = new UserEntity();
        persistentUser.setId(1l);
        persistentUser.setUsername(Users.USERNAME);
        persistentUser.setPassword(Users.EMAIL);
        given(userRepository.findOneByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(persistentUser));
    }

    @Test
    @DisplayName("Doesn't log in using the email a expired user")
    void testLogIn_Email_AccountExpired() {
        final LoginStatus  status;
        final LoginRequest login;

        loadUser();

        login = new LoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getServiceForAccountExpired().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a user with expired credentials")
    void testLogIn_Email_CredentialsExpired() {
        final LoginStatus  status;
        final LoginRequest login;

        loadUser();

        login = new LoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getServiceForCredentialsExpired().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a disabled user")
    void testLogIn_Email_Disabled() {
        final LoginStatus  status;
        final LoginRequest login;

        loadUser();

        login = new LoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getServiceForDisabled().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a locked user")
    void testLogIn_Email_Locked() {
        final LoginStatus  status;
        final LoginRequest login;

        loadUser();

        login = new LoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getServiceForLocked().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the email a not existing user")
    void testLogIn_Email_NotExisting() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getServiceForNotExisting().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid email")
    void testLogIn_Email_Valid() {
        final LoginStatus  status;
        final LoginRequest login;

        loadUser();

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn("token");

        login = new LoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getServiceForValid().login(login);

        Assertions.assertThat(status.getLogged())
            .isTrue();
    }

    @Test
    @DisplayName("Doesn't log in using the username a expired user")
    void testLogIn_Username_AccountExpired() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getServiceForAccountExpired().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a user with expired credentials")
    void testLogIn_Username_CredentialsExpired() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getServiceForCredentialsExpired().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a disabled user")
    void testLogIn_Username_Disabled() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getServiceForDisabled().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a locked user")
    void testLogIn_Username_Locked() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getServiceForLocked().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in using the username a not existing user")
    void testLogIn_Username_NotExisting() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getServiceForNotExisting().login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid username")
    void testLogIn_Username_Valid() {
        final LoginStatus  status;
        final LoginRequest login;

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(true);
        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn("token");

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getServiceForValid().login(login);

        Assertions.assertThat(status.getLogged())
            .isTrue();
    }

}
