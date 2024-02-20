
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.test.config.factory.RolesQuery;
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
    @SingleRole
    void testFindAll() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("When there are no roles nothing is returned")
    void testFindAll_NoData() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When there are roles with no granted permissions they are returned")
    @RoleWithCrudPermissionsNotGranted
    void testFindAll_WithNotGrantedPermission() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("When there are roles with permissions they are returned")
    @RoleWithCrudPermissions
    void testFindAll_WithPermissions() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withPermissions());
    }

    @Test
    @DisplayName("When there are roles with a single permission they are returned")
    @RoleWithPermission
    void testFindAll_WithSinglePermission() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withSinglePermission());
    }

}
