
package com.bernardomg.security.initializer.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.initializer.usecase.service.DefaultRolesInitializerService;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.Roles;

@ExtendWith(MockitoExtension.class)
@DisplayName("RolesInitializer")
class TestRolesInitializer {

    @Mock
    private ResourcePermissionRepository   resourcePermissionRepository;

    @Mock
    private RolePermissionRepository       rolePermissionRepository;

    @Mock
    private RoleRepository                 roleRepository;

    @InjectMocks
    private DefaultRolesInitializerService rolesInitializerService;

    @BeforeEach
    public final void initializeMocks() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.read());
        given(resourcePermissionRepository.findAll()).willReturn(permissions);
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Creates roles")
    void testInitialize_Roles() {
        // WHEN
        rolesInitializerService.initialize();

        // THEN
        verify(roleRepository, atLeastOnce()).save(ArgumentMatchers.any());
    }

}
