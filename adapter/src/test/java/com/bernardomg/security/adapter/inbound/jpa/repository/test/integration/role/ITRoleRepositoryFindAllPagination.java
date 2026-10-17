
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.pagination.PaginationTestSupport;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.test.config.role.factory.RoleFilters;
import com.bernardomg.security.adapter.test.config.role.factory.Roles;
import com.bernardomg.security.domain.role.filter.RoleFilter;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.repository.RoleRepository;

@DisplayName("RoleRepository - find all - pagination")
@IntegrationTest
@RoleWithoutPermissions
@Disabled("Comparison fails due to audit")
class ITRoleRepositoryFindAllPagination extends PaginationTestSupport<Role> {

    @Autowired
    private RoleRepository repository;

    ITRoleRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Page<Role> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return repository.findAll(RoleFilters.empty(), pagination, sorting);
    }

    @Test
    @DisplayName("When the first page is requested, then all page data is returned")
    void testFindAll_Page1_Data() {
        final Page<Role> roles;
        final Pagination pagination;

        // GIVEN
        pagination = new Pagination(1, 1);

        // WHEN
        roles = read(pagination);

        // THEN
        assertPageContents(roles, Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When the second page is requested, then all page data is returned")
    void testFindAll_Page2_Data() {
        final RoleFilter sample;
        final Page<Role> roles;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(2, 1);
        sorting = Sorting.unsorted();

        sample = RoleFilters.empty();

        // WHEN
        roles = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .as("When the second page is requested, then all page data is returned (roles)")
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .isEmpty();
    }

    @Test
    @DisplayName("When a page is returned, then its metadata matches the request and result")
    void testReadPaged_PageData() {
        final Page<Role> roles;
        final Pagination pagination;

        // GIVEN
        pagination = new Pagination(1, 1);

        // WHEN
        roles = read(pagination);

        // THEN
        assertPageMetadata(roles, 1);
    }

    @Test
    @DisplayName("When a page size is requested, then the response contains that many records")
    void testReadPaged_PageMax() {
        final Page<Role> roles;
        final Pagination pagination;

        // GIVEN
        pagination = new Pagination(1, 1);

        // WHEN
        roles = read(pagination);

        // THEN
        assertPageSize(roles);
    }

}
