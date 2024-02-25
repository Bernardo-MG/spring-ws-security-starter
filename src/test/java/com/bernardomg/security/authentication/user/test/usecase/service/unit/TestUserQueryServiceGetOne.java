
package com.bernardomg.security.authentication.user.test.usecase.service.unit;

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

import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserQueryService;

@ExtendWith(MockitoExtension.class)
@DisplayName("User service - get one")
class TestUserQueryServiceGetOne {

    @InjectMocks
    private DefaultUserQueryService service;

    @Mock
    private UserRepository          userRepository;

    public TestUserQueryServiceGetOne() {
        super();
    }

    @Test
    @DisplayName("When there is a user it is returned")
    void testGetOne() {
        final Optional<User> user;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);
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
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        execution = () -> service.getOne(UserConstants.USERNAME);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserUsernameException.class);
    }

}
