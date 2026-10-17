
package com.bernardomg.security.usecase.test.role.service.unit;

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

import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;
import com.bernardomg.security.domain.role.exception.MissingRoleException;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.usecase.role.service.DefaultRoleService;
import com.bernardomg.security.usecase.test.role.config.factory.RoleConstants;
import com.bernardomg.security.usecase.test.role.config.factory.Roles;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - delete")
class TestDefaultRoleServiceDelete {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultRoleService           service;

    @Test
    @DisplayName("When deleting a role, then the repository is called")
    void testDelete() {

        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.of(Roles.withSinglePermission()));

        // WHEN
        service.delete(RoleConstants.NAME);

        // THEN
        verify(roleRepository).delete(RoleConstants.NAME);
    }

    @Test
    @DisplayName("When deleting a nonexistent role, then an exception is thrown")
    void testDelete_NotExisting() {
        final ThrowingCallable action;

        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.empty());

        // WHEN
        action = () -> service.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When deleting a nonexistent role, then an exception is thrown (action)")
            .isInstanceOf(MissingRoleException.class);
    }

}
