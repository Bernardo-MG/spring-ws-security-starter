
package com.bernardomg.security.user.data.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.data.usecase.service.DefaultUserService;
import com.bernardomg.security.user.notification.usecase.notificator.UserNotificator;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - register new user")
class TestUserServiceRegisterNewUser {

    @Mock
    private PasswordEncoder    passwordEncoder;

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultUserService service;

    @Mock
    private UserTokenStore     tokenStore;

    @Mock
    private UserNotificator    userNotificator;

    @Mock
    private UserRepository     userRepository;

    @Test
    @DisplayName("Sends the user to the repository, ignoring case")
    void testRegisterNewUser_Case_AddsEntity() {
        // GIVEN
        given(userRepository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.registerNewUser(UserConstants.USERNAME.toUpperCase(), UserConstants.NAME,
            UserConstants.EMAIL.toUpperCase());

        // THEN
        verify(userRepository).newUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, ignoring case")
    void testRegisterNewUser_Case_ReturnedData() {
        final User user;

        // GIVEN
        given(userRepository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.registerNewUser(UserConstants.USERNAME.toUpperCase(), UserConstants.NAME,
            UserConstants.EMAIL.toUpperCase());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("Throws an exception when the email already exists")
    void testRegisterNewUser_ExistingEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.existsByEmail(UserConstants.EMAIL)).willReturn(true);

        // WHEN
        executable = () -> service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        failure = new FieldFailure("existing", "email", "email.existing", UserConstants.EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    void testRegisterNewUser_ExistingUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        failure = new FieldFailure("existing", "username", "username.existing", UserConstants.USERNAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Sends the user to the repository, padded with whitespace")
    void testRegisterNewUser_Padded_AddsEntity() {
        // GIVEN
        given(userRepository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.registerNewUser(" " + UserConstants.USERNAME + " ", " " + UserConstants.NAME + " ",
            " " + UserConstants.EMAIL + " ");

        // THEN
        verify(userRepository).newUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, padded with whitespace")
    void testRegisterNewUser_Padded_ReturnedData() {
        final User user;

        // GIVEN
        given(userRepository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.registerNewUser(" " + UserConstants.USERNAME + " ", " " + UserConstants.NAME + " ",
            " " + UserConstants.EMAIL + " ");

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("Sends the user to the repository")
    void testRegisterNewUser_PersistedData() {
        // GIVEN
        given(userRepository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        verify(userRepository).newUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user")
    void testRegisterNewUser_ReturnedData() {
        final User user;

        // GIVEN
        given(userRepository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

}
