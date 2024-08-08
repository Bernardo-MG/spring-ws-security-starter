
package com.bernardomg.security.permission.test.adapter.inbound.initializer.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.permission.data.adapter.inbound.initializer.ResourcePermissionPair;
import com.bernardomg.security.permission.data.domain.repository.ActionRepository;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.data.domain.repository.ResourceRepository;
import com.bernardomg.security.permission.initializer.usecase.PermissionRegister;
import com.bernardomg.security.permission.initializer.usecase.PermissionsLoader;
import com.bernardomg.security.permission.test.config.factory.Actions;
import com.bernardomg.security.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.permission.test.config.factory.Resources;

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

        // GIVEN
        given(actionRepository.exists(PermissionConstants.CREATE)).willReturn(false);
        given(resourceRepository.exists(PermissionConstants.DATA)).willReturn(false);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(false);

        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(actionRepository).save(List.of(Actions.create()));
        verify(resourceRepository).save(List.of(Resources.data()));
        verify(resourcePermissionRepository).save(List.of(ResourcePermissions.create()));
    }

    @Test
    @DisplayName("When there is no data nothing is stored")
    void testLoad_NoData() {
        // WHEN
        getPermissionsLoaderNoData().load();

        // THEN
        verify(actionRepository).save(List.of());
        verify(resourceRepository).save(List.of());
        verify(resourcePermissionRepository).save(List.of());
    }

}
