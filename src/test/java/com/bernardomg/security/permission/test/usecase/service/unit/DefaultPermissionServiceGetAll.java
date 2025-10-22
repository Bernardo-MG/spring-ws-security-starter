
package com.bernardomg.security.permission.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.permission.usecase.service.DefaultPermissionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultPermissionService - get all")
class DefaultPermissionServiceGetAll {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @InjectMocks
    private DefaultPermissionService     service;

    @Test
    @DisplayName("When there are permissions they are returned")
    void testGetAll() {
        final Page<ResourcePermission> read;
        final Pagination               pagination;
        final Sorting                  sorting;
        final Page<ResourcePermission> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = new Page<>(List.of(ResourcePermissions.read()), 0, 0, 0, 0, 0, false, false, sorting);
        given(resourcePermissionRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        read = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(read)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .containsExactly(ResourcePermissions.read());
    }

    @Test
    @DisplayName("When there are no permissions nothing is returned")
    void testGetAll_NoData() {
        final Page<ResourcePermission> read;
        final Pagination               pagination;
        final Sorting                  sorting;
        final Page<ResourcePermission> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = new Page<>(List.of(), 0, 0, 0, 0, 0, false, false, sorting);
        given(resourcePermissionRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        read = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(read)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .isEmpty();
    }

}
