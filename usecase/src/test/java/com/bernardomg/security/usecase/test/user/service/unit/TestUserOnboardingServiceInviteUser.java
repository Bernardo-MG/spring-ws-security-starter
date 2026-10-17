
package com.bernardomg.security.usecase.test.user.service.unit;

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

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.event.model.SecurityEventFactory;
import com.bernardomg.security.domain.role.exception.MissingRoleException;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.domain.user.event.UserInvitationEvent;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.user.config.factory.RoleConstants;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;
import com.bernardomg.security.usecase.token.TokenValidator;
import com.bernardomg.security.usecase.token.UserTokenStore;
import com.bernardomg.security.usecase.user.service.DefaultUserOnboardingService;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserOnboardingService - invite user")
class TestUserOnboardingServiceInviteUser {

    @Mock
    private EventEmitter                 eventEmitter;

    @Mock
    private PasswordEncrypter            passwordEncrypt;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultUserOnboardingService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Mock
    private TokenValidator               tokenValidator;

    @Mock
    private UserRepository               userRepository;

    @Test
    @DisplayName("When inviting a user with case-insensitive input, then the user is sent to the repository")
    void testInviteUser_Case_AddsEntity() {
        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(Users.upperCase());

        // THEN
        verify(userRepository).save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When inviting a user with case-insensitive input, then the created user is returned")
    void testInviteUser_Case_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(Users.withoutRoles());

        // THEN
        Assertions.assertThat(user)
            .as("When inviting a user with case-insensitive input, then the created user is returned (user)")
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("When inviting a user with a duplicated role, then a validation error is raised")
    void testInviteUser_DuplicatedRole() {
        final ThrowingCallable action;
        final FieldFailure     failure;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        action = () -> service.inviteUser(Users.duplicatedRole());

        // THEN
        failure = new FieldFailure("duplicated", "roles[]", "roles[].duplicated", 1L);

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When inviting a user with an existing email, then a validation error is raised")
    void testInviteUser_ExistingEmail() {
        final ThrowingCallable action;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.existsByEmail(UserConstants.EMAIL)).willReturn(true);

        // WHEN
        action = () -> service.inviteUser(Users.withoutRoles());

        // THEN
        failure = new FieldFailure("existing", "email", "email.existing", UserConstants.EMAIL);

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When inviting a user with an existing username, then a validation error is raised")
    void testInviteUser_ExistingUsername() {
        final ThrowingCallable action;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        action = () -> service.inviteUser(Users.withoutRoles());

        // THEN
        failure = new FieldFailure("existing", "username", "username.existing", UserConstants.USERNAME);

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When inviting a user with an invalid email, then a validation error is raised")
    void testInviteUser_InvalidEmail() {
        final ThrowingCallable action;
        final FieldFailure     failure;

        // WHEN
        action = () -> service.inviteUser(Users.invalidEmail());

        // THEN
        failure = new FieldFailure("invalid", "email", "email.invalid", "abc");

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When the role does not exist, then an exception is thrown")
    void testInviteUser_NotExistingRole() {
        final ThrowingCallable action;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        action = () -> service.inviteUser(Users.enabled());

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the role does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingRoleException.class);
    }

    @Test
    @DisplayName("When a user is invited, then an invitation event is sent")
    void testInviteUser_Notification() {
        final UserInvitationEvent userInvitationEvent;

        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());
        userInvitationEvent = SecurityEventFactory.userInvitation(Users.newlyCreated(), Tokens.TOKEN);

        // WHEN
        service.inviteUser(Users.withoutRoles());

        // THEN
        verify(eventEmitter)
            .emit(org.mockito.ArgumentMatchers.argThat(actual -> actual instanceof final UserInvitationEvent emitted
                    && emitted.getRecipient()
                        .equals(userInvitationEvent.getRecipient())
                    && emitted.getToken()
                        .equals(userInvitationEvent.getToken())
                    && emitted.getSource()
                        .equals(userInvitationEvent.getSource())));
    }

    @Test
    @DisplayName("When inviting a user with padded input, then the user is sent to the repository")
    void testInviteUser_Padded_AddsEntity() {
        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(Users.padded());

        // THEN
        verify(userRepository).save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When inviting a user with padded input, then the created user is returned")
    void testInviteUser_Padded_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(Users.padded());

        // THEN
        Assertions.assertThat(user)
            .as("When inviting a user with padded input, then the created user is returned (user)")
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("When inviting a user with roles, then the user is sent to the repository")
    void testInviteUser_Role_PersistedData() {
        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.save(Users.newlyCreatedWithRole(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreatedWithRole());

        // WHEN
        service.inviteUser(Users.withRole());

        // THEN
        verify(userRepository).save(Users.newlyCreatedWithRole(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When inviting a user with roles, then the created user is returned")
    void testInviteUser_Role_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.save(Users.newlyCreatedWithRole(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreatedWithRole());

        // WHEN
        user = service.inviteUser(Users.withRole());

        // THEN
        Assertions.assertThat(user)
            .as("When inviting a user with roles, then the created user is returned (user)")
            .isEqualTo(Users.newlyCreatedWithRole());
    }

    @Test
    @DisplayName("When inviting a user without roles, then the user is sent to the repository")
    void testInviteUser_WithoutRoles_PersistedData() {
        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(Users.withoutRoles());

        // THEN
        verify(userRepository).save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When inviting a user without roles, then the created user is returned")
    void testInviteUser_WithoutRoles_ReturnedData() {
        final User user;

        // GIVEN
        given(passwordEncrypt.encrypt("")).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.save(Users.newlyCreated(), UserConstants.ENCODED_PASSWORD))
            .willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(Users.withoutRoles());

        // THEN
        Assertions.assertThat(user)
            .as("When inviting a user without roles, then the created user is returned (user)")
            .isEqualTo(Users.newlyCreated());
    }

}
