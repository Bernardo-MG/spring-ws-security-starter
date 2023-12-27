
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
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.security.login.model.TokenLoginStatus;
import com.bernardomg.security.login.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.service.LoginTokenEncoder;
import com.bernardomg.security.login.service.TokenLoginService;
import com.bernardomg.security.login.service.springframework.SpringValidLoginPredicate;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - token generation")
class TestTokenLoginServiceToken {

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

    public TestTokenLoginServiceToken() {
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
    @DisplayName("Returns a token login status when the user is logged")
    void testLogin_Logged() {
        final TokenLoginStatus status;

        loadUser();

        given(tokenEncoder.encode(ArgumentMatchers.any())).willReturn(UserTokens.TOKEN);

        status = getService(true).login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
        Assertions.assertThat(status.getToken())
            .isEqualTo(UserTokens.TOKEN);
    }

    @Test
    @DisplayName("Returns a default login status when the user is logged")
    void testLogin_NotLogged() {
        final TokenLoginStatus status;

        loadUser();

        status = getService(false).login(UserConstants.EMAIL, UserConstants.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

}
