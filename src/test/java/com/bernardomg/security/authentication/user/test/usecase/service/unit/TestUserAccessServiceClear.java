
package com.bernardomg.security.authentication.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserAccessService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserAccessService - clear login attempts")
class TestUserAccessServiceClear {

    private DefaultUserAccessService service;

    @Mock
    private UserRepository           userRepository;

    public TestUserAccessServiceClear() {
        super();
    }

    @BeforeEach
    public void setupService() {
        service = new DefaultUserAccessService(UserConstants.MAX_LOGIN_ATTEMPTS, userRepository);
    }

    @Test
    @DisplayName("When the user has reached the max login attempts, these are cleared")
    void testCheckForLocking_MaxAttempts() {
        // GIVEN
        given(userRepository.getLoginAttempts(UserConstants.USERNAME)).willReturn(UserConstants.MAX_LOGIN_ATTEMPTS);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        verify(userRepository).update(Users.enabled());
    }

    @Test
    @DisplayName("When the user has no login attempts, nothing is done")
    void testCheckForLocking_NoAttempts() {
        // GIVEN
        given(userRepository.getLoginAttempts(UserConstants.USERNAME)).willReturn(0);

        // WHEN
        service.checkForLocking(UserConstants.USERNAME);

        // THEN
        verify(userRepository, Mockito.never()).update(ArgumentMatchers.any());
    }

}
