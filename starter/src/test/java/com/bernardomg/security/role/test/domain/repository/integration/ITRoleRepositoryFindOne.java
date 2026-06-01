
package com.bernardomg.security.role.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.role.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - find one")
class ITRoleRepositoryFindOne {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindOne() {
        super();
    }

    @Test
    @DisplayName("When the role exists it is returned")
    @RoleWithoutPermissions
    void testFindOne_Existing() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(role)
            .contains(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testFindOne_NoData() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // WHILE
        Assertions.assertThat(role)
            .isEmpty();
    }

    @Test
    @DisplayName("When the role doesn't exist nothing is returned")
    @RoleWithoutPermissions
    void testFindOne_NotExisting() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne("abc");

        // WHILE
        Assertions.assertThat(role)
            .isEmpty();
    }

    @Test
    @DisplayName("When the role exists, and it has permissions, it is returned")
    @RoleWithCrudPermissions
    void testFindOne_WithPermissions() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(role)
            .contains(Roles.withPermissions());
    }

    @Test
    @DisplayName("When the role exists, and it has a permission, it is returned")
    @RoleWithPermission
    void testFindOne_WithSinglePermission() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(role)
            .contains(Roles.withSinglePermission());
    }

}
