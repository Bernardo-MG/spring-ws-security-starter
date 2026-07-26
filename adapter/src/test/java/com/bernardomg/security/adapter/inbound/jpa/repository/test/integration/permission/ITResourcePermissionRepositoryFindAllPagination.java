
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.permission;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.pagination.AbstractPaginationIT;
import com.bernardomg.security.adapter.test.config.permission.annotation.SinglePermission;
import com.bernardomg.security.adapter.test.config.permission.factory.ResourcePermissions;
import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;

@DisplayName("ResourcePermissionRepository - find all - pagination")
@SinglePermission
class ITResourcePermissionRepositoryFindAllPagination extends AbstractPaginationIT<ResourcePermission> {

    @Autowired
    private ResourcePermissionRepository repository;

    public ITResourcePermissionRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Page<ResourcePermission> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return repository.findAll(pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAll_Page1_Data() {
        testPageData(1, ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAll_Page2_Data() {
        final Page<ResourcePermission> permissions;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(2, 1);
        sorting = Sorting.unsorted();

        // WHEN
        permissions = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .isEmpty();
    }

}
