
package com.bernardomg.security.usecase.test.login.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.login.service.DefaultUserLoginAttempsService;
import com.bernardomg.security.usecase.login.service.UserLoginAttempsService;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserLoginAttempsService - check for locking")
class TestDefaultUserLoginAttempsServiceCheckForLocking {

    private UserLoginAttempsService service;

    @Mock
    private UserRepository          userRepository;

    public TestDefaultUserLoginAttempsServiceCheckForLocking() {
        super();
    }

    @BeforeEach
    public void setupService() {
        service = new DefaultUserLoginAttempsService(UserConstants.MAX_LOGIN_ATTEMPTS, userRepository);
    }

    @Test
    @DisplayName("When this is the first login attempt, then the user is not locked")
    void testCheckForLocking_FirstAttempt() {
        // GIVEN
        given(userRepository.increaseLoginAttempts(UserConstants.USERNAME)).willReturn(1);

        // WHEN
        service.checkForLocking(UserConstants.USERNAME);

        // THEN
        verify(userRepository, Mockito.never()).lock(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When the user is below the maximum login-attempt limit, then the user is not locked")
    void testCheckForLocking_JustUnderMaxAttempts() {
        // GIVEN
        given(userRepository.increaseLoginAttempts(UserConstants.USERNAME))
            .willReturn(UserConstants.MAX_LOGIN_ATTEMPTS - 1);

        // WHEN
        service.checkForLocking(UserConstants.USERNAME);

        // THEN
        verify(userRepository, Mockito.never()).lock(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When the user has reached the maximum login-attempt limit, then the user is locked")
    void testCheckForLocking_MaxAttempts() {
        // GIVEN
        given(userRepository.increaseLoginAttempts(UserConstants.USERNAME))
            .willReturn(UserConstants.MAX_LOGIN_ATTEMPTS);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.checkForLocking(UserConstants.USERNAME);

        // THEN
        verify(userRepository).lock(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When the user does not exist, then the user is not locked")
    void testCheckForLocking_NoUser() {
        // GIVEN
        given(userRepository.increaseLoginAttempts(UserConstants.USERNAME)).willReturn(0);

        // WHEN
        service.checkForLocking(UserConstants.USERNAME);

        // THEN
        verify(userRepository, Mockito.never()).lock(UserConstants.USERNAME);
    }

}
