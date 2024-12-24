
package com.bernardomg.security.role.test.domain.repository.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@DisplayName("RolePermissionRepository - find available permissions - pagination")
@CrudPermissions
@RoleWithoutPermissions
class ITRolePermissionRepositoryFindAvailablePermissionsPagination extends AbstractPaginationIT<ResourcePermission> {

    @Autowired
    private RolePermissionRepository repository;

    public ITRolePermissionRepositoryFindAvailablePermissionsPagination() {
        super(4, "name");
    }

    @Override
    protected final Iterable<ResourcePermission> read(final Pageable pageable) {
        final Pagination pagination;
        final Sorting    sorting;

        pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());
        sorting = Sorting.unsorted();
        return repository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAvailablePermissions_Page1_Data() {
        testPageData(0, ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAvailablePermissions_Page2_Data() {
        testPageData(1, ResourcePermissions.delete());
    }

}
