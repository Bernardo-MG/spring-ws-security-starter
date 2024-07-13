
package com.bernardomg.security.password.test.reset.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.jwt.test.config.Tokens;
import com.bernardomg.security.password.notification.usecase.notification.PasswordNotificator;
import com.bernardomg.security.password.reset.usecase.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;
import com.bernardomg.test.config.factory.UserDetailsData;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - change password")
class TestSpringSecurityPasswordResetServiceChange {

    @Mock
    private PasswordEncoder                    passwordEncoder;

    @Mock
    private PasswordNotificator                passwordNotificator;

    @InjectMocks
    private SpringSecurityPasswordResetService service;

    @Mock
    private UserTokenStore                     tokenStore;

    @Mock
    private UserDetailsService                 userDetailsService;

    @Mock
    private UserRepository                     userRepository;

    public TestSpringSecurityPasswordResetServiceChange() {
        super();
    }

    @Test
    @DisplayName("Changing password when the user is expired resets the flag")
    void testChangePassword_PasswordExpired_Persisted() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpired()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(UserDetailsData.valid());

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(userRepository).resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);
    }

    @Test
    @DisplayName("Changing password sends the data to the repository")
    void testChangePassword_Persisted() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(UserDetailsData.valid());

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(userRepository).resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);
    }

    @Test
    @DisplayName("Changing password consumes the token")
    void testChangePassword_TokenConsumed() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(UserDetailsData.valid());

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(tokenStore).consumeToken(Tokens.TOKEN);
    }

}
