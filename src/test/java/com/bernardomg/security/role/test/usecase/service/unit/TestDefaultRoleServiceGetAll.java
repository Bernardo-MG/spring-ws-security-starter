
package com.bernardomg.security.role.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.role.test.config.factory.RolesQuery;
import com.bernardomg.security.role.usecase.service.DefaultRoleService;

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
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;
        final Iterable<Role> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.empty();

        existing = List.of(Roles.withPermissions());
        given(roleRepository.findAll(sample, pagination, sorting)).willReturn(existing);

        // WHEN
        roles = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withPermissions());
    }

    @Test
    @DisplayName("When there are no roles nothing is returned")
    void testGetAll_NoData() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;
        final Iterable<Role> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.empty();

        existing = List.of();
        given(roleRepository.findAll(sample, pagination, sorting)).willReturn(existing);

        // WHEN
        roles = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
