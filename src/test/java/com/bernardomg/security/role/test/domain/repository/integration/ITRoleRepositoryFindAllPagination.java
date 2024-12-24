
package com.bernardomg.security.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.role.test.config.factory.RolesQuery;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@DisplayName("RoleRepository - find all - pagination")
@RoleWithoutPermissions
class ITRoleRepositoryFindAllPagination extends AbstractPaginationIT<Role> {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Iterable<Role> read(final Pageable pageable) {
        final Pagination pagination;
        final Sorting    sorting;

        pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());
        sorting = Sorting.unsorted();
        return repository.findAll(RolesQuery.empty(), pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAll_Page1_Data() {
        testPageData(0, Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAll_Page2_Data() {
        final RoleQuery      sample;
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 1);
        sorting = Sorting.unsorted();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
