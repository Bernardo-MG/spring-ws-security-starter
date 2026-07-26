
package com.bernardomg.security.usecase.test.role.service.unit;

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

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;
import com.bernardomg.security.domain.role.filter.RoleFilter;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.usecase.role.service.DefaultRoleService;
import com.bernardomg.security.usecase.test.role.config.factory.RoleFilters;
import com.bernardomg.security.usecase.test.role.config.factory.Roles;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - get all")
class TestDefaultRoleServiceGetAll {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultRoleService           service;

    @Test
    @DisplayName("When there are roles they are returned")
    void testGetAll() {
        final Page<Role> roles;
        final RoleFilter sample;
        final Pagination pagination;
        final Sorting    sorting;
        final Page<Role> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RoleFilters.empty();

        existing = new Page<>(List.of(Roles.withPermissions()), 0, 0, 0, 0, 0, false, false, sorting);
        given(roleRepository.findAll(sample, pagination, sorting)).willReturn(existing);

        // WHEN
        roles = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .containsExactly(Roles.withPermissions());
    }

    @Test
    @DisplayName("When there are no roles nothing is returned")
    void testGetAll_NoData() {
        final Page<Role> roles;
        final RoleFilter sample;
        final Pagination pagination;
        final Sorting    sorting;
        final Page<Role> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RoleFilters.empty();

        existing = new Page<>(List.of(), 0, 0, 0, 0, 0, false, false, sorting);
        given(roleRepository.findAll(sample, pagination, sorting)).willReturn(existing);

        // WHEN
        roles = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .isEmpty();
    }

}
