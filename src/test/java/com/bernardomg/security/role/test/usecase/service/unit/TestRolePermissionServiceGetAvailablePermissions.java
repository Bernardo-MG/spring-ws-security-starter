
package com.bernardomg.security.role.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.role.domain.exception.MissingRoleException;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.security.role.usecase.service.DefaultRolePermissionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRolePermissionService - get available permissions")
class TestRolePermissionServiceGetAvailablePermissions {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private RolePermissionRepository     rolePermissionRepository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultRolePermissionService service;

    @Test
    @DisplayName("When there are available permissions they are returned")
    void testGetAvailablePermissions() {
        final Page<ResourcePermission> permissions;
        final Page<ResourcePermission> existing;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        existing = new Page<>(List.of(ResourcePermissions.read()), 0, 0, 0, 0, 0, false, false, sorting);
        given(rolePermissionRepository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting))
            .willReturn(existing);

        // WHEN
        permissions = service.getAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .containsOnly(ResourcePermissions.read());
    }

    @Test
    @DisplayName("When there are no available permissions nothing is returned")
    void testGetAvailablePermissions_NoData() {
        final Page<ResourcePermission> permissions;
        final Page<ResourcePermission> existing;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        existing = new Page<>(List.of(), 0, 0, 0, 0, 0, false, false, sorting);
        given(rolePermissionRepository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting))
            .willReturn(existing);

        // WHEN
        permissions = service.getAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    void testGetAvailablePermissions_NoRole() {
        final ThrowingCallable executable;
        final Pagination       pagination;
        final Sorting          sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        executable = () -> service.getAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleException.class);
    }

}
