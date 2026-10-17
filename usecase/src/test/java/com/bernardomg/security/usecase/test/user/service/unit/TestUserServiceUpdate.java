
package com.bernardomg.security.usecase.test.user.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.role.exception.MissingRoleException;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.domain.user.exception.MissingUsernameException;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.test.user.config.factory.RoleConstants;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;
import com.bernardomg.security.usecase.token.UserTokenStore;
import com.bernardomg.security.usecase.user.service.DefaultUserService;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - update")
class TestUserServiceUpdate {

    @Mock
    private EventEmitter       eventEmitter;

    @Mock
    private PasswordEncrypter  passwordEncrypter;

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultUserService service;

    @Mock
    private UserTokenStore     tokenStore;

    @Mock
    private UserRepository     userRepository;

    @Test
    @DisplayName("When updating a user with a duplicated role, then a validation error is raised")
    void testUpdate_DuplicatedRole() {
        final ThrowingCallable action;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        action = () -> service.update(Users.duplicatedRole());

        // THEN
        failure = new FieldFailure("duplicated", "roles[]", "roles[].duplicated", 1L);

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When updating a user with an existing email, then a validation error is raised")
    void testUpdate_ExistingMail() {
        final ThrowingCallable action;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.existsEmailForAnotherUser(ArgumentMatchers.eq(UserConstants.USERNAME),
            ArgumentMatchers.anyString())).willReturn(true);

        // WHEN
        action = () -> service.update(Users.emailChange());

        // THEN
        failure = new FieldFailure("existing", "email", "email.existing", UserConstants.ALTERNATIVE_EMAIL);

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When updating a user with an invalid email, then a validation error is raised")
    void testUpdate_InvalidMail() {
        final ThrowingCallable action;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        action = () -> service.update(Users.invalidEmail());

        // THEN
        failure = new FieldFailure("invalid", "email", "email.invalid", "abc");

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When the role does not exist, then an exception is thrown")
    void testUpdate_NotExistingRole() {
        final ThrowingCallable action;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        action = () -> service.update(Users.enabled());

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the role does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingRoleException.class);
    }

    @Test
    @DisplayName("When the user does not exist, then an exception is thrown")
    void testUpdate_NotExistingUser() {
        final ThrowingCallable action;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        action = () -> service.update(Users.enabled());

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the user does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingUsernameException.class);
    }

    @Test
    @DisplayName("When updating a user's email, then the user is sent to the repository")
    void testUpdate_UpdateEmail_PersistedData() {

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        service.update(Users.emailChange());

        // THEN
        verify(userRepository).save(Users.emailChange());
    }

    @Test
    @DisplayName("When updating a user's email, then the updated user is returned")
    void testUpdate_UpdateEmail_ReturnedData() {
        final User result;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.save(Users.emailChange())).willReturn(Users.emailChange());

        // WHEN
        result = service.update(Users.emailChange());

        // THEN
        Assertions.assertThat(result)
            .as("When updating a user's email, then the updated user is returned (result)")
            .isEqualTo(Users.emailChange());
    }

    @Test
    @DisplayName("When updating a user without roles, then the user is sent to the repository")
    void testUpdate_withoutRoles_PersistedData() {

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.update(Users.withoutRoles());

        // THEN
        verify(userRepository).save(Users.withoutRoles());
    }

    @Test
    @DisplayName("When updating a user without roles, then the updated user is returned")
    void testUpdate_withoutRoles_ReturnedData() {
        final User result;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userRepository.save(Users.withoutRoles())).willReturn(Users.withoutRoles());

        // WHEN
        result = service.update(Users.withoutRoles());

        // THEN
        Assertions.assertThat(result)
            .as("When updating a user without roles, then the updated user is returned (result)")
            .isEqualTo(Users.withoutRoles());
    }

}
