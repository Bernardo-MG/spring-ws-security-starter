
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@IntegrationTest
@DisplayName("RolePermissionRepository - find available permissions - pagination")
@CrudPermissions
@RoleWithoutPermissions
class ITRolePermissionRepositoryFindAvailablePermissionsPagination extends AbstractPaginationIT<ResourcePermission> {

    @Autowired
    private RolePermissionRepository repository;

    @Override
    protected final Iterable<ResourcePermission> read(final Pageable pageable) {
        return repository.findAvailablePermissions(RoleConstants.NAME, pageable);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAvailablePermissions_Page1_Data() {
        testPageData(0, ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAvailablePermissions_Page2_Data() {
        testPageData(1, ResourcePermissions.read());
    }

}
