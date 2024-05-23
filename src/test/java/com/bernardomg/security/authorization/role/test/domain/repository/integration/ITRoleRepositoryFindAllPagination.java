
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.RoleQuery;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.test.config.factory.RolesQuery;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@IntegrationTest
@DisplayName("RoleRepository - find all - pagination")
@RoleWithoutPermissions
class ITRoleRepositoryFindAllPagination extends AbstractPaginationIT<Role> {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindAllPagination() {
        super();
    }

    @Override
    protected final Iterable<Role> read(final Pageable pageable) {
        return repository.findAll(RolesQuery.empty(), pageable);
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
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
