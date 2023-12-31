
package com.bernardomg.security.authentication.password.test.reset.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.password.notification.PasswordNotificator;
import com.bernardomg.security.authentication.password.reset.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserEntities;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.token.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordRecoveryService - Mail generation on recovery start")
class TestSpringSecurityPasswordResetServiceStartEmail {

    @Mock
    private PasswordEncoder                    passwordEncoder;

    @Mock
    private PasswordNotificator                passwordNotificator;

    @Mock
    private UserRepository                     repository;

    @InjectMocks
    private SpringSecurityPasswordResetService service;

    @Mock
    private UserTokenStore                     tokenProcessor;

    @Mock
    private UserTokenStore                     tokenStore;

    @Mock
    private UserDetailsService                 userDetailsService;

    public TestSpringSecurityPasswordResetServiceStartEmail() {
        super();
    }

    @BeforeEach
    void initializeUser() {
        final UserEntity  user;
        final UserDetails details;

        user = UserEntities.enabled();

        details = new User(Users.USERNAME, "password", true, true, true, true, Collections.emptyList());

        given(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).willReturn(details);

        given(repository.findOneByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(user));

        // given(tokenStore.createToken(ArgumentMatchers.anyString())).willReturn(Tokens.TOKEN);
    }

    @Test
    @DisplayName("When recovering the password the email is sent to the user email")
    void testStartPasswordRecovery_Email() {
        final ArgumentCaptor<String> emailCaptor;

        emailCaptor = ArgumentCaptor.forClass(String.class);

        // WHEN
        service.startPasswordReset(Users.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(emailCaptor.capture(), ArgumentMatchers.any(),
            ArgumentMatchers.any());

        Assertions.assertThat(emailCaptor.getValue())
            .as("email")
            .isEqualTo(Users.EMAIL);
    }

    @Test
    @DisplayName("When recovering the password the token is sent to the user email")
    @Disabled("The stubbing is failing")
    void testStartPasswordRecovery_Token() {
        final ArgumentCaptor<String> tokenCaptor;

        tokenCaptor = ArgumentCaptor.forClass(String.class);

        // WHEN
        service.startPasswordReset(Users.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(ArgumentMatchers.any(), ArgumentMatchers.any(),
            tokenCaptor.capture());

        Assertions.assertThat(tokenCaptor.getValue())
            .as("token")
            .isEqualTo(Tokens.TOKEN);
    }

    @Test
    @DisplayName("When recovering the password the email is sent to the user email")
    void testStartPasswordRecovery_Username() {
        final ArgumentCaptor<String> usernameCaptor;

        usernameCaptor = ArgumentCaptor.forClass(String.class);

        // WHEN
        service.startPasswordReset(Users.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(ArgumentMatchers.any(), usernameCaptor.capture(),
            ArgumentMatchers.any());

        Assertions.assertThat(usernameCaptor.getValue())
            .as("username")
            .isEqualTo(Users.USERNAME);
    }

}
