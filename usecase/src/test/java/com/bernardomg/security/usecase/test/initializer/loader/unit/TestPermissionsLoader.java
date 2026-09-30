
package com.bernardomg.security.usecase.test.initializer.loader.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.permission.repository.ActionRepository;
import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;
import com.bernardomg.security.domain.permission.repository.ResourceRepository;
import com.bernardomg.security.usecase.initializer.domain.model.PermissionConfig;
import com.bernardomg.security.usecase.initializer.domain.model.ResourcePermissionConfig;
import com.bernardomg.security.usecase.initializer.loader.DefaultPermissionsLoaderService;
import com.bernardomg.security.usecase.initializer.loader.PermissionConfigLoader;
import com.bernardomg.security.usecase.initializer.loader.PermissionsLoaderService;
import com.bernardomg.security.usecase.test.permission.config.factory.Actions;
import com.bernardomg.security.usecase.test.permission.config.factory.PermissionConstants;
import com.bernardomg.security.usecase.test.permission.config.factory.ResourcePermissions;
import com.bernardomg.security.usecase.test.permission.config.factory.Resources;

@ExtendWith(MockitoExtension.class)
@DisplayName("PermissionsLoader")
public class TestPermissionsLoader {

    @Mock
    private ActionRepository             actionRepository;

    @Mock
    private ResourcePermissionConfig     permission;

    @Mock
    private PermissionConfig             permissionConfig;

    @Mock
    private PermissionConfigLoader       permissionConfigLoader;

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private ResourceRepository           resourceRepository;

    private PermissionsLoaderService     service;

    private final ResourcePermissionConfig getPermission() {
        final ResourcePermissionConfig permission;

        permission = new ResourcePermissionConfig();
        permission.setResource("data");
        permission.setActions(List.of("create"));

        return permission;
    }

    @BeforeEach
    void setupConfig() {}

    @BeforeEach
    void setupService() {
        service = new DefaultPermissionsLoaderService(actionRepository, resourceRepository,
            resourcePermissionRepository, permissionConfigLoader);
    }

    @Test
    @DisplayName("When an action does not exist, then it is saved")
    void testLoad_Action() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAll()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(actionRepository).saveAll(List.of(Actions.create()));
    }

    @Test
    @DisplayName("When an action already exists, then it is not saved again")
    void testLoad_ActionExists() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of(PermissionConstants.CREATE));
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(actionRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("When permissions are loaded with duplicates, then duplicate entries are removed")
    @SuppressWarnings("unchecked")
    void testLoad_Duplicates() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of(), List.of(PermissionConstants.CREATE));
        given(resourceRepository.findAllNames()).willReturn(List.of(), List.of(PermissionConstants.DATA));
        given(resourcePermissionRepository.findAll()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission(), getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(actionRepository).saveAll(List.of(Actions.create()));
        verify(resourceRepository).saveAll(List.of(Resources.data()));
        verify(resourcePermissionRepository).saveAll(List.of(ResourcePermissions.create()));
    }

    @Test
    @DisplayName("When no permission data is provided, then nothing is saved")
    void testLoad_NoData() {
        final PermissionConfig config;

        // GIVEN
        config = mock(PermissionConfig.class);
        given(config.getActions()).willReturn(List.of());
        given(config.getPermissions()).willReturn(List.of());
        given(permissionConfigLoader.load()).willReturn(List.of(config));

        // WHEN
        service.load();

        // THEN
        verify(actionRepository).saveAll(List.of());
        verify(resourceRepository).saveAll(List.of());
        verify(resourcePermissionRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("When no permission source is provided, then nothing is saved")
    void testLoad_NoSource() {

        // GIVEN
        given(permissionConfigLoader.load()).willReturn(List.of());

        // WHEN
        service.load();

        // THEN
        verify(actionRepository).saveAll(List.of());
        verify(resourceRepository).saveAll(List.of());
        verify(resourcePermissionRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("When a permission does not exist, then it is saved")
    void testLoad_Permission() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of(PermissionConstants.CREATE));
        given(resourceRepository.findAllNames()).willReturn(List.of(PermissionConstants.DATA));
        given(resourcePermissionRepository.findAll()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(resourcePermissionRepository).saveAll(List.of(ResourcePermissions.create()));
    }

    @Test
    @DisplayName("When a permission's action does not exist, then nothing is saved")
    void testLoad_Permission_NoActions() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of(PermissionConstants.DATA));
        given(resourcePermissionRepository.findAll()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(resourcePermissionRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("When a permission's resource does not exist, then nothing is saved")
    void testLoad_Permission_NoResource() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of(PermissionConstants.CREATE));
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAll()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(resourcePermissionRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("When a permission already exists, then it is not saved again")
    void testLoad_PermissionExists() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAll()).willReturn(List.of(ResourcePermissions.create()));
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(resourcePermissionRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("When a resource does not exist, then it is saved")
    void testLoad_Resource() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAll()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(resourceRepository).saveAll(List.of(Resources.data()));
    }

    @Test
    @DisplayName("When a resource already exists, then it is not saved again")
    void testLoad_ResourceExists() {
        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of(PermissionConstants.DATA));
        given(resourcePermissionRepository.findAll()).willReturn(List.of());
        given(permissionConfig.getActions()).willReturn(List.of("create"));
        given(permissionConfig.getPermissions()).willReturn(List.of(getPermission()));
        given(permissionConfigLoader.load()).willReturn(List.of(permissionConfig));

        // WHEN
        service.load();

        // THEN
        verify(resourceRepository).saveAll(List.of());
    }

}
