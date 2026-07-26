
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.pagination.AbstractPaginationIT;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.test.config.role.factory.Roles;
import com.bernardomg.security.adapter.test.config.role.factory.RolesQuery;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.model.RoleQuery;
import com.bernardomg.security.domain.role.repository.RoleRepository;

@DisplayName("RoleRepository - find all - pagination")
@RoleWithoutPermissions
class ITRoleRepositoryFindAllPagination extends AbstractPaginationIT<Role> {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Page<Role> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return repository.findAll(RolesQuery.empty(), pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAll_Page1_Data() {
        testPageData(1, Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAll_Page2_Data() {
        final RoleQuery  sample;
        final Page<Role> roles;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(2, 1);
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

}
