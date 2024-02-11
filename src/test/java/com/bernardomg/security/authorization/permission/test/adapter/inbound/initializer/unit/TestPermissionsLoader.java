
package com.bernardomg.security.authorization.permission.test.adapter.inbound.initializer.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authorization.permission.adapter.inbound.initializer.PermissionRegister;
import com.bernardomg.security.authorization.permission.adapter.inbound.initializer.PermissionsLoader;
import com.bernardomg.security.authorization.permission.adapter.inbound.initializer.ResourcePermissionPair;
import com.bernardomg.security.authorization.permission.domain.repository.ActionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.ResourceRepository;
import com.bernardomg.security.authorization.permission.test.config.factory.Actions;
import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.Resources;

@ExtendWith(MockitoExtension.class)
@DisplayName("TestPermissions")
public class TestPermissionsLoader {

    @Mock
    private ActionRepository             actionRepository;

    @Mock
    private PermissionRegister           permissionRegister;

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private ResourceRepository           resourceRepository;

    private final PermissionsLoader getPermissionsLoader() {

        // GIVEN
        given(permissionRegister.getActions()).willReturn(List.of(PermissionConstants.CREATE));
        given(permissionRegister.getResources()).willReturn(List.of(PermissionConstants.DATA));
        given(permissionRegister.getPermissions())
            .willReturn(List.of(ResourcePermissionPair.of(PermissionConstants.DATA, PermissionConstants.CREATE)));

        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(false);

        return new PermissionsLoader(actionRepository, resourceRepository, resourcePermissionRepository,
            List.of(permissionRegister));
    }

    private final PermissionsLoader getPermissionsLoaderNoData() {

        // GIVEN
        given(permissionRegister.getActions()).willReturn(List.of());
        given(permissionRegister.getResources()).willReturn(List.of());
        given(permissionRegister.getPermissions()).willReturn(List.of());

        return new PermissionsLoader(actionRepository, resourceRepository, resourcePermissionRepository,
            List.of(permissionRegister));
    }

    @Test
    @DisplayName("When loading the permissions are stored")
    void testLoad() {
        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(actionRepository).save(Actions.create());
        verify(resourceRepository).save(Resources.data());
        verify(resourcePermissionRepository).save(ResourcePermissions.create());
    }

    @Test
    @DisplayName("When there is no data nothing is stored")
    void testLoad_NoData() {
        // WHEN
        getPermissionsLoaderNoData().load();

        // THEN
        verify(actionRepository, Mockito.never()).save(ArgumentMatchers.any());
        verify(resourceRepository, Mockito.never()).save(ArgumentMatchers.any());
        verify(resourcePermissionRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

}
