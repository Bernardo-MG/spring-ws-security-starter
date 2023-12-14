
package com.bernardomg.security.login.test.service.unit;

import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.util.function.BiPredicate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.login.model.TokenLoginStatus;
import com.bernardomg.security.login.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.service.LoginTokenEncoder;
import com.bernardomg.security.login.service.TokenLoginService;
import com.bernardomg.security.login.service.springframework.SpringValidLoginPredicate;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenLoginService - failure handling")
class TestTokenLoginServiceFailure {

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

    public TestTokenLoginServiceFailure() {
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

    private final TokenLoginService getServiceWithNullUser() {
        return getService(null);
    }

    @Test
    @DisplayName("When the user details service returns a null the login fails")
    void testLogIn_NullUser() {
        final TokenLoginStatus status;

        status = getServiceWithNullUser().login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

}
