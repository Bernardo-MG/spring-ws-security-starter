
package com.bernardomg.security.usecase.test.user.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.domain.user.exception.MissingUsernameException;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;
import com.bernardomg.security.usecase.token.UserTokenStore;
import com.bernardomg.security.usecase.user.service.DefaultUserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - delete")
class TestUserServiceDelete {

    @Mock
    private EventEmitter       eventEmitter;

    @Mock
    private PasswordEncrypter  passwordEncrypt;

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultUserService service;

    @Mock
    private UserTokenStore     tokenStore;

    @Mock
    private UserRepository     userRepository;

    @Test
    @DisplayName("When deleting a user, then it is removed")
    void testDelete() {
        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.delete(UserConstants.USERNAME);

        // THEN
        verify(userRepository).delete(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When deleting a nonexistent user, then an exception is thrown")
    void testDelete_NotExisting() {
        final ThrowingCallable action;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        action = () -> service.delete(UserConstants.USERNAME);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When deleting a nonexistent user, then an exception is thrown (action)")
            .isInstanceOf(MissingUsernameException.class);
    }

}
