
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.annotation.RoleWithPermission;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.factory.Roles;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.factory.RolesQuery;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.model.RoleQuery;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - find all")
class ITRoleRepositoryFindAll {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindAll() {
        super();
    }

    @Test
    @DisplayName("When there are roles they are returned")
    @RoleWithoutPermissions
    void testFindAll() {
        final Page<Role> roles;
        final RoleQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When there are no roles nothing is returned")
    void testFindAll_NoData() {
        final Page<Role> roles;
        final RoleQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .isEmpty();
    }

    @Test
    @DisplayName("When there are roles with permissions they are returned")
    @RoleWithCrudPermissions
    void testFindAll_WithPermissions() {
        final Page<Role> roles;
        final RoleQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .containsExactly(Roles.withPermissions());
    }

    @Test
    @DisplayName("When there are roles with a single permission they are returned")
    @RoleWithPermission
    void testFindAll_WithSinglePermission() {
        final Page<Role> roles;
        final RoleQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .containsExactly(Roles.withSinglePermission());
    }

}
