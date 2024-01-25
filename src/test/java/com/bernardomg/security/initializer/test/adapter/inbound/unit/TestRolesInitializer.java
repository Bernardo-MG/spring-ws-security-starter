
package com.bernardomg.security.initializer.test.adapter.inbound.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.initializer.adapter.inbound.RolesInitializer;
import com.bernardomg.security.login.domain.model.LoginRegister;

@ExtendWith(MockitoExtension.class)
@DisplayName("RolesInitializer")
class TestRolesInitializer {

    @Captor
    private ArgumentCaptor<LoginRegister> loginRegisterCaptor;

    @Mock
    private ResourcePermissionRepository  resourcePermissionRepository;

    @Mock
    private RolePermissionRepository      rolePermissionRepository;

    @Mock
    private RoleRepository                roleRepository;

    @InjectMocks
    private RolesInitializer              rolesInitializer;

    @BeforeEach
    public final void initializeMocks() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.read());
        given(resourcePermissionRepository.findAll()).willReturn(permissions);
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.valid());
    }

    @Test
    @DisplayName("Sets permissions to roles")
    void testRun_Permissions() {
        // WHEN
        rolesInitializer.initialize();

        // THEN
        verify(rolePermissionRepository, atLeastOnce()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Creates roles")
    void testRun_Roles() {
        // WHEN
        rolesInitializer.initialize();

        // THEN
        verify(roleRepository, atLeastOnce()).save(ArgumentMatchers.any());
    }

}
