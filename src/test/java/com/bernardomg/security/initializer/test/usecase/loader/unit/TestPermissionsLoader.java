
package com.bernardomg.security.initializer.test.usecase.loader.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.initializer.usecase.loader.PermissionsLoader;
import com.bernardomg.security.permission.domain.repository.ActionRepository;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.domain.repository.ResourceRepository;
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
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private ResourceRepository           resourceRepository;

    private final PermissionsLoader getPermissionsLoader() {
        final String      yaml;
        final InputStream inputStream;

        // GIVEN
        yaml = """
                actions:
                  - create
                permissions:
                  - resource: data
                    actions:
                      - create
                """;
        inputStream = new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8));

        return new PermissionsLoader(actionRepository, resourceRepository, resourcePermissionRepository, inputStream);
    }

    private final PermissionsLoader getPermissionsLoaderNoData() {
        final String      yaml;
        final InputStream inputStream;

        // GIVEN
        yaml = """
                """;
        inputStream = new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8));

        return new PermissionsLoader(actionRepository, resourceRepository, resourcePermissionRepository, inputStream);
    }

    private final PermissionsLoader getPermissionsLoaderWithDuplicates() {
        final String      yaml;
        final InputStream inputStream;

        // GIVEN
        yaml = """
                actions:
                  - create
                permissions:
                  - resource: data
                    actions:
                      - create
                  - resource: data
                    actions:
                      - create
                """;
        inputStream = new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8));

        return new PermissionsLoader(actionRepository, resourceRepository, resourcePermissionRepository, inputStream);
    }

    @Test
    @DisplayName("When the action doesn't exist it is saved")
    void testLoad_Action() {

        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAllNames()).willReturn(List.of());

        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(actionRepository).save(List.of(Actions.create()));
    }

    @Test
    @DisplayName("When the action exists it is not saved")
    void testLoad_ActionExists() {

        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of(PermissionConstants.CREATE));
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAllNames()).willReturn(List.of());

        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(actionRepository).save(List.of());
    }

    @Test
    @DisplayName("When loading the permissions duplicates are removed")
    void testLoad_Duplicates() {

        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAllNames()).willReturn(List.of());

        // WHEN
        getPermissionsLoaderWithDuplicates().load();

        // THEN
        verify(actionRepository).save(List.of(Actions.create()));
        verify(resourceRepository).save(List.of(Resources.data()));
        verify(resourcePermissionRepository).save(List.of(ResourcePermissions.create()));
    }

    @Test
    @DisplayName("When there is no data nothing is saved")
    void testLoad_NoData() {
        // WHEN
        getPermissionsLoaderNoData().load();

        // THEN
        verify(actionRepository).save(List.of());
        verify(resourceRepository).save(List.of());
        verify(resourcePermissionRepository).save(List.of());
    }

    @Test
    @DisplayName("When the permission doesn't exist it is saved")
    void testLoad_Permission() {

        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAllNames()).willReturn(List.of());

        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(resourcePermissionRepository).save(List.of(ResourcePermissions.create()));
    }

    @Test
    @DisplayName("When the permissions exists it is not saved")
    void testLoad_PermissionExists() {

        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAllNames()).willReturn(List.of(PermissionConstants.DATA_CREATE));

        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(resourcePermissionRepository).save(List.of());
    }

    @Test
    @DisplayName("When the resource doesn't exist it is saved")
    void testLoad_Resource() {

        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of());
        given(resourcePermissionRepository.findAllNames()).willReturn(List.of());

        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(resourceRepository).save(List.of(Resources.data()));
    }

    @Test
    @DisplayName("When the resource exists it is not saved")
    void testLoad_ResourceExists() {

        // GIVEN
        given(actionRepository.findAllNames()).willReturn(List.of());
        given(resourceRepository.findAllNames()).willReturn(List.of(PermissionConstants.DATA));
        given(resourcePermissionRepository.findAllNames()).willReturn(List.of());

        // WHEN
        getPermissionsLoader().load();

        // THEN
        verify(resourceRepository).save(List.of());
    }

}
