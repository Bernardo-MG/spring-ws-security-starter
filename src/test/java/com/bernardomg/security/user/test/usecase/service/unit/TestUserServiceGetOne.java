
package com.bernardomg.security.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.service.DefaultUserService;
import com.bernardomg.security.user.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("User service - get one")
class TestUserServiceGetOne {

    @Mock
    private EventEmitter       eventEmitter;

    @Mock
    private PasswordEncoder    passwordEncoder;

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultUserService service;

    @Mock
    private UserTokenStore     tokenStore;

    @Mock
    private UserRepository     userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    public TestUserServiceGetOne() {
        super();
    }

    @Test
    @DisplayName("When there is a user it is returned")
    void testGetOne() {
        final Optional<User> user;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        user = service.getOne(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(user)
            .as("users")
            .contains(Users.enabled());
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testGetOne_NoData() {
        final ThrowingCallable execution;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        execution = () -> service.getOne(UserConstants.USERNAME);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUsernameException.class);
    }

}
