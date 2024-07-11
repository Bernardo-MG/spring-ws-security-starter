
package com.bernardomg.security.authentication.user.test.usecase.service.unit;

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

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authentication.user.usecase.notification.UserNotificator;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserActivationService;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - register new user")
class TestUserActivationServiceRegisterNewUser {

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

    @Test
    @DisplayName("Sends the user to the repository, ignoring case")
    void testRegisterNewUser_Case_AddsEntity() {
        // GIVEN
        given(repository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.registerNewUser(UserConstants.USERNAME.toUpperCase(), UserConstants.NAME,
            UserConstants.EMAIL.toUpperCase());

        // THEN
        verify(repository).newUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, ignoring case")
    void testRegisterNewUser_Case_ReturnedData() {
        final User user;

        // GIVEN
        given(repository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

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
        given(repository.existsByEmail(UserConstants.EMAIL)).willReturn(true);

        // WHEN
        executable = () -> service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        failure = FieldFailure.of("email.existing", "email", "existing", UserConstants.EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    void testRegisterNewUser_ExistingUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        failure = FieldFailure.of("username.existing", "username", "existing", UserConstants.USERNAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Sends the user to the repository, padded with whitespace")
    void testRegisterNewUser_Padded_AddsEntity() {
        // GIVEN
        given(repository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.registerNewUser(" " + UserConstants.USERNAME + " ", " " + UserConstants.NAME + " ",
            " " + UserConstants.EMAIL + " ");

        // THEN
        verify(repository).newUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, padded with whitespace")
    void testRegisterNewUser_Padded_ReturnedData() {
        final User user;

        // GIVEN
        given(repository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

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
        given(repository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        verify(repository).newUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user")
    void testRegisterNewUser_ReturnedData() {
        final User user;

        // GIVEN
        given(repository.newUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

}
