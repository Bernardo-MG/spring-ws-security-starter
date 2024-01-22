
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.login.usecase.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.TokenLoginService;
import com.bernardomg.security.login.usecase.service.springframework.SpringValidLoginPredicate;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - password validation")
class TestTokenLoginServicePassword {

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

    public TestTokenLoginServicePassword() {
        super();
    }

    private final TokenLoginService getService(final Boolean match) {
        final UserDetails                 user;
        final BiPredicate<String, String> valid;
        final LoginTokenEncoder           loginTokenEncoder;

        user = new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, true, true, true,
            Collections.emptyList());

        given(userDetService.loadUserByUsername(ArgumentMatchers.anyString())).willReturn(user);

        given(passEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(match);

        valid = new SpringValidLoginPredicate(userDetService, passEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, resourcePermissionRepository,
            Duration.ZERO);

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventPublisher);
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
    @DisplayName("Doesn't log in using the email and with an invalid password")
    void testLogIn_Email_InvalidPassword() {
        final TokenLoginStatus status;

        loadUser();

        status = getService(false).login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in using the email and with a valid password")
    void testLogIn_Email_ValidPassword() {
        final TokenLoginStatus status;

        loadUser();

        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        status = getService(true).login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
    }

    @Test
    @DisplayName("Doesn't log in using the username and with an invalid password")
    void testLogIn_Username_InvalidPassword() {
        final TokenLoginStatus status;

        status = getService(false).login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in using the username and with a valid password")
    void testLogIn_Username_ValidPassword() {
        final TokenLoginStatus status;

        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(Tokens.TOKEN);

        status = getService(true).login(UserConstants.USERNAME, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
    }

}
