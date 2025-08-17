
package com.bernardomg.security.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.role.test.config.annotation.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.role.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.role.test.config.factory.RolesQuery;
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
    @DisplayName("When there are roles with no granted permissions they are returned")
    @RoleWithCrudPermissionsNotGranted
    void testFindAll_WithNotGrantedPermission() {
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
