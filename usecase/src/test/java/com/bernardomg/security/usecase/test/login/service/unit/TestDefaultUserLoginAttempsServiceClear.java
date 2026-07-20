
package com.bernardomg.security.usecase.test.login.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.login.service.DefaultUserLoginAttempsService;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserLoginAttempsService - clear login attempts")
class TestDefaultUserLoginAttempsServiceClear {

    private DefaultUserLoginAttempsService service;

    @Mock
    private UserRepository                 userRepository;

    public TestDefaultUserLoginAttempsServiceClear() {
        super();
    }

    @BeforeEach
    public void setupService() {
        service = new DefaultUserLoginAttempsService(UserConstants.MAX_LOGIN_ATTEMPTS, userRepository);
    }

    @Test
    @DisplayName("When the user has reached the max login attempts, these are cleared")
    void testCheckForLocking_MaxAttempts() {
        // GIVEN
        given(userRepository.findLoginAttempts(UserConstants.USERNAME)).willReturn(UserConstants.MAX_LOGIN_ATTEMPTS);

        // WHEN
        service.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        verify(userRepository).clearLoginAttempts(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When the user has no login attempts, nothing is done")
    void testCheckForLocking_NoAttempts() {
        // GIVEN
        given(userRepository.findLoginAttempts(UserConstants.USERNAME)).willReturn(0);

        // WHEN
        service.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        verify(userRepository, Mockito.never()).clearLoginAttempts(UserConstants.USERNAME);
    }

}
