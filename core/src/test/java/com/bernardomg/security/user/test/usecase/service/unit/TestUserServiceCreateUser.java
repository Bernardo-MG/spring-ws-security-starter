
package com.bernardomg.security.user.test.usecase.service.unit;

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

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.role.domain.exception.MissingRoleException;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.service.DefaultUserService;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - create")
class TestUserServiceCreateUser {

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

    @Test
    @DisplayName("Sends the user to the repository, ignoring case")
    void testCreate_Case_AddsEntity() {
        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        service.create(Users.upperCase());

        // THEN
        verify(userRepository).save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Returns the created user, ignoring case")
    void testCreate_Case_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        user = service.create(Users.upperCase());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("Throws an exception when the role is duplicated")
    void testCreate_DuplicatedRole() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        executable = () -> service.create(Users.duplicatedRole());

        // THEN
        failure = new FieldFailure("duplicated", "roles[]", "roles[].duplicated", 1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the email already exists")
    void testCreate_ExistingEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.existsByEmail(UserConstants.EMAIL)).willReturn(true);

        // WHEN
        executable = () -> service.create(Users.withoutRoles());

        // THEN
        failure = new FieldFailure("existing", "email", "email.existing", UserConstants.EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    void testCreate_ExistingUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.create(Users.withoutRoles());

        // THEN
        failure = new FieldFailure("existing", "username", "username.existing", UserConstants.USERNAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the email has an invalid format")
    void testCreate_InvalidEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // WHEN
        executable = () -> service.create(Users.invalidEmail());

        // THEN
        failure = new FieldFailure("invalid", "email", "email.invalid", "abc");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("When the role doesn't exists an exception is thrown")
    void testCreate_NotExistingRole() {
        final ThrowingCallable execution;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        execution = () -> service.create(Users.enabled());

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingRoleException.class);
    }

    @Test
    @DisplayName("Sends the user to the repository, padded with whitespace")
    void testCreate_Padded_AddsEntity() {
        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        service.create(Users.padded());

        // THEN
        verify(userRepository).save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Returns the created user, padded with whitespace")
    void testCreate_Padded_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        user = service.create(Users.padded());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("With a user with roles, it is sent to the repository")
    void testCreate_Role_PersistedData() {
        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.save(Users.newlyCreatedWithRole(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        service.create(Users.withRole());

        // THEN
        verify(userRepository).save(Users.newlyCreatedWithRole(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("With a user with roles, it is returned")
    void testCreate_Role_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.save(Users.newlyCreatedWithRole(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreatedWithRole());

        // WHEN
        user = service.create(Users.withRole());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreatedWithRole());
    }

    @Test
    @DisplayName("With a user without roles, it is sent to the repository")
    void testCreate_WithoutRoles_PersistedData() {
        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreatedWithRole());

        // WHEN
        service.create(Users.withoutRoles());

        // THEN
        verify(userRepository).save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("With a user without roles, it is returned")
    void testCreate_WithoutRoles_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncoder.encode("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        user = service.create(Users.withoutRoles());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

}
