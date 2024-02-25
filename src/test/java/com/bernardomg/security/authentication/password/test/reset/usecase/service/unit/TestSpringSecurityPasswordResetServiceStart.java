
package com.bernardomg.security.authentication.password.test.reset.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.password.reset.usecase.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.authentication.password.usecase.notification.PasswordNotificator;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.token.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - change password")
class TestSpringSecurityPasswordResetServiceStart {

    @Mock
    private PasswordEncoder                    passwordEncoder;

    @Mock
    private PasswordNotificator                passwordNotificator;

    @InjectMocks
    private SpringSecurityPasswordResetService service;

    @Mock
    private UserTokenStore                     tokenStore;

    @Mock
    private UserDetails                        userDetails;

    @Mock
    private UserDetailsService                 userDetailsService;

    @Mock
    private UserRepository                     userRepository;

    public TestSpringSecurityPasswordResetServiceStart() {
        super();
    }

    @Test
    @DisplayName("When recovering the password the correct message arguments are used")
    void testStartPasswordReset_Message() {
        // GIVEN
        given(userDetails.isAccountNonExpired()).willReturn(true);
        given(userDetails.isAccountNonLocked()).willReturn(true);
        given(userDetails.isEnabled()).willReturn(true);

        given(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).willReturn(userDetails);
        given(userRepository.findOneByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(Users.enabled()));
        given(tokenStore.createToken(ArgumentMatchers.anyString())).willReturn(Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(UserConstants.EMAIL, UserConstants.USERNAME,
            Tokens.TOKEN);
    }

    @Test
    @DisplayName("When starting the password reset the token is regenerated")
    void testStartPasswordReset_NewToken() {
        // GIVEN
        given(userDetails.isAccountNonExpired()).willReturn(true);
        given(userDetails.isAccountNonLocked()).willReturn(true);
        given(userDetails.isEnabled()).willReturn(true);

        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(tokenStore).revokeExistingTokens(UserConstants.USERNAME);
        verify(tokenStore).createToken(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When there is no user an exception is thrown")
    void testStartPasswordReset_NoUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.empty());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing id mail@somewhere.com for user");
    }

    @Test
    @DisplayName("When starting the password reset a message is sent to the user")
    void testStartPasswordReset_SendMessage() {
        // GIVEN
        given(userDetails.isAccountNonExpired()).willReturn(true);
        given(userDetails.isAccountNonLocked()).willReturn(true);
        given(userDetails.isEnabled()).willReturn(true);

        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(UserConstants.EMAIL, UserConstants.USERNAME,
            Tokens.TOKEN);
    }

}
