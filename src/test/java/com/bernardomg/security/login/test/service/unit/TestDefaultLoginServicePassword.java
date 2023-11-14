
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.permission.persistence.repository.UserGrantedPermissionRepository;
import com.bernardomg.security.login.model.LoginStatus;
import com.bernardomg.security.login.model.request.DtoLoginRequest;
import com.bernardomg.security.login.model.request.LoginRequest;
import com.bernardomg.security.login.service.DefaultLoginService;
import com.bernardomg.security.login.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.service.LoginTokenEncoder;
import com.bernardomg.security.login.service.springframework.SpringValidLoginPredicate;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultLoginService - password validation")
class TestDefaultLoginServicePassword {

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

    public TestDefaultLoginServicePassword() {
        super();
    }

    private final DefaultLoginService getService(final Boolean match) {
        final UserDetails             user;
        final Predicate<LoginRequest> valid;
        final LoginTokenEncoder       loginTokenEncoder;

        user = new User("username", "password", true, true, true, true, Collections.emptyList());

        given(userDetService.loadUserByUsername(ArgumentMatchers.anyString())).willReturn(user);

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(match);

        valid = new SpringValidLoginPredicate(userDetService, passEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, userGrantedPermissionRepository,
            Duration.ZERO);

        return new DefaultLoginService(valid, userRepository, loginTokenEncoder);
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
    @DisplayName("Doesn't log in using the email and with an invalid password")
    void testLogIn_Email_InvalidPassword() {
        final LoginStatus     status;
        final DtoLoginRequest login;

        loadUser();

        login = new DtoLoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getService(false).login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
        Assertions.assertThat(status.getUsername())
            .isEqualTo(Users.USERNAME);
    }

    @Test
    @DisplayName("Logs in using the email and with a valid password")
    void testLogIn_Email_ValidPassword() {
        final LoginStatus     status;
        final DtoLoginRequest login;

        loadUser();

        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn("token");

        login = new DtoLoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = getService(true).login(login);

        Assertions.assertThat(status.getLogged())
            .isTrue();
        Assertions.assertThat(status.getUsername())
            .isEqualTo(Users.USERNAME);
    }

    @Test
    @DisplayName("Doesn't log in using the username and with an invalid password")
    void testLogIn_Username_InvalidPassword() {
        final LoginStatus     status;
        final DtoLoginRequest login;

        login = new DtoLoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getService(false).login(login);

        Assertions.assertThat(status.getLogged())
            .isFalse();
        Assertions.assertThat(status.getUsername())
            .isEqualTo(Users.USERNAME);
    }

    @Test
    @DisplayName("Logs in using the username and with a valid password")
    void testLogIn_Username_ValidPassword() {
        final LoginStatus     status;
        final DtoLoginRequest login;

        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn("token");

        login = new DtoLoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = getService(true).login(login);

        Assertions.assertThat(status.getLogged())
            .isTrue();
        Assertions.assertThat(status.getUsername())
            .isEqualTo(Users.USERNAME);
    }

}
