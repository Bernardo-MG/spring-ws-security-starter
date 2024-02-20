
package com.bernardomg.security.authorization.role.test.usecase.service.unit;

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

import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleException;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.DefaultUserRoleService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserRoleService - remove role")
class TestDefaultUserRoleServiceRemoveRole {

    @Mock
    private RoleRepository         roleRepository;

    @InjectMocks
    private DefaultUserRoleService service;

    @Mock
    private UserRepository         userRepository;

    @Mock
    private UserRoleRepository     userRoleRepository;

    public TestDefaultUserRoleServiceRemoveRole() {
        super();
    }

    @Test
    @DisplayName("Removing roles calls the repository")
    void testRemoveRole() {
        final Optional<Role> existing;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        existing = Optional.of(Roles.valid());
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(existing);

        // WHEN
        service.removeRole(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        verify(userRoleRepository).delete(UserConstants.USERNAME, RoleConstants.NAME);
    }

    @Test
    @DisplayName("Deleting a not existing role throws an exception")
    void testRemoveRole_NotExistingRole() {
        final Optional<Role>   existing;
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        existing = Optional.empty();
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(existing);

        // WHEN
        executable = () -> service.removeRole(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleException.class);
    }

    @Test
    @DisplayName("Deleting a role for a not existing user throws an exception")
    void testRemoveRole_NotExistingUser() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        executable = () -> service.removeRole(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserUsernameException.class);
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemoveRole_ReturnedData() {
        final Role           role;
        final Optional<Role> existing;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        existing = Optional.of(Roles.valid());
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(existing);

        // WHEN
        role = service.removeRole(UserConstants.USERNAME, RoleConstants.NAME);

        Assertions.assertThat(role)
            .isEqualTo(Roles.valid());
    }

}
