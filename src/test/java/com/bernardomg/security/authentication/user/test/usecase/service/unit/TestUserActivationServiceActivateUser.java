
package com.bernardomg.security.authentication.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authentication.user.usecase.notification.UserNotificator;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserActivationService;
import com.bernardomg.security.authorization.token.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - activate user")
class TestUserActivationServiceActivateUser {

    @Mock
    private PasswordEncoder              passwordEncoder;

    @Mock
    private UserRepository               repository;

    @InjectMocks
    private DefaultUserActivationService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Mock
    private UserNotificator              userNotificator;

    public TestUserActivationServiceActivateUser() {
        super();
    }

    @Test
    @DisplayName("Activating a new user consumes the token")
    void testActivateUser_ConsumesToken() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreated()));
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(tokenStore).consumeToken(Tokens.TOKEN);
    }

    @Test
    @DisplayName("Activating a disabled user saves it as enabled")
    void testActivateUser_Disabled() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.disabled()));
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).save(Users.enabled(), UserConstants.PASSWORD);
    }

    @Test
    @DisplayName("Activating a new user keeps its roles")
    void testActivateUser_KeepsRoles() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreatedWithRole()));
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).save(Users.enabled(), UserConstants.PASSWORD);
    }

    @Test
    @DisplayName("Activating a new user saves it as enabled")
    void testActivateUser_NewlyCreated() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreated()));
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).save(Users.withoutRoles(), UserConstants.PASSWORD);
    }

    @Test
    @DisplayName("Activating a user with password expired saves it as enabled")
    void testActivateUser_PasswordExpired() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpiredAndDisabled()));
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).save(Users.enabled(), UserConstants.PASSWORD);
    }

}
