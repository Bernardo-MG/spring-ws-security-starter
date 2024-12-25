
package com.bernardomg.security.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.role.test.config.factory.RolesQuery;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - find all - filter")
class ITRoleRepositoryFindAllFilter {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindAllFilter() {
        super();
    }

    @Test
    @DisplayName("When filtering by name the correct role is returned")
    @RoleWithoutPermissions
    void testFindAll_FilterByName() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.byName();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When filtering by a not existing name nothing is returned")
    @RoleWithoutPermissions
    void testFindAll_FilterByName_NotExiting() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.byNameNotExisting();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When filtering by name and there is no data nothing is returned")
    void testFindAll_NoData() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = RolesQuery.byName();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
