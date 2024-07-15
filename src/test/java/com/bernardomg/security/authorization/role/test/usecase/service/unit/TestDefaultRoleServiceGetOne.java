
package com.bernardomg.security.authorization.role.test.usecase.service.unit;

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

import com.bernardomg.security.authorization.role.domain.exception.MissingRoleException;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.DefaultRoleService;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - get one")
class TestDefaultRoleServiceGetOne {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultRoleService           service;

    public TestDefaultRoleServiceGetOne() {
        super();
    }

    @Test
    @DisplayName("When the role exists it is returned")
    void testGetOne() {
        final Optional<Role> existing;
        final Optional<Role> role;

        // GIVEN
        existing = Optional.of(Roles.withPermissions());
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(existing);

        // WHEN
        role = service.getOne(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(role)
            .contains(Roles.withPermissions());
    }

    @Test
    @DisplayName("When the role doesn't exist an exception is thrown")
    void testGetOne_NoData() {
        final ThrowingCallable execution;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        execution = () -> service.getOne(RoleConstants.NAME);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingRoleException.class);
    }

}
