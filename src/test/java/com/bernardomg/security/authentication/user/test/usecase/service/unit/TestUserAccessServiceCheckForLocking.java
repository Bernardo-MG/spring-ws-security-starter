
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
@DisplayName("UserAccessService - check for locking")
class TestUserAccessServiceCheckForLocking {

    private DefaultUserAccessService service;

    @Mock
    private UserRepository           userRepository;

    public TestUserAccessServiceCheckForLocking() {
        super();
    }

    @BeforeEach
    public void setupService() {
        service = new DefaultUserAccessService(UserConstants.MAX_LOGIN_ATTEMPTS, userRepository);
    }

    @Test
    @DisplayName("When the user has reached the max login attempts it is locked")
    void testCheckForLocking_MaxAttempts() {
        // GIVEN
        given(userRepository.getLoginAttempts(UserConstants.USERNAME)).willReturn(UserConstants.MAX_LOGIN_ATTEMPTS);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.checkForLocking(UserConstants.USERNAME);

        // THEN
        verify(userRepository, Mockito.never()).update(Users.locked());
    }

    @Test
    @DisplayName("When the user has no login attempts it is not locked")
    void testCheckForLocking_NoAttempts() {
        // GIVEN
        given(userRepository.getLoginAttempts(UserConstants.USERNAME)).willReturn(0);

        // WHEN
        service.checkForLocking(UserConstants.USERNAME);

        // THEN
        verify(userRepository, Mockito.never()).update(ArgumentMatchers.any());
    }

}
