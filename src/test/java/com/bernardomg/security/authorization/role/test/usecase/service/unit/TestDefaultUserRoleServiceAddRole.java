
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
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.DefaultUserRoleService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserRoleService - add role")
class TestDefaultUserRoleServiceAddRole {

    @Mock
    private RoleRepository         roleRepository;

    @InjectMocks
    private DefaultUserRoleService service;

    @Mock
    private UserRepository         userRepository;

    @Mock
    private UserRoleRepository     userRoleRepository;

    public TestDefaultUserRoleServiceAddRole() {
        super();
    }

    @Test
    @DisplayName("When the role doesn't exist an exception is thrown")
    void testAddRole_NoRole() {
        final ThrowingCallable execution;
        final Optional<Role>   role;

        // GIVEN
        role = Optional.empty();
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(role);
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        execution = () -> service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingRoleNameException.class);
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testAddRole_NoUser() {
        final ThrowingCallable execution;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        execution = () -> service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserUsernameException.class);
    }

    @Test
    @DisplayName("Sends the data to the repository")
    void testAddRole_PersistedData() {
        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.of(Roles.valid()));
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        verify(userRoleRepository).save(UserConstants.USERNAME, RoleConstants.NAME);
    }

    @Test
    @DisplayName("Returns the created data")
    void testAddRole_ReturnedData() {
        final Role           userRole;
        final Optional<Role> role;

        // GIVEN
        role = Optional.of(Roles.valid());
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(role);
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        userRole = service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        Assertions.assertThat(userRole)
            .isEqualTo(Roles.valid());
    }

}
