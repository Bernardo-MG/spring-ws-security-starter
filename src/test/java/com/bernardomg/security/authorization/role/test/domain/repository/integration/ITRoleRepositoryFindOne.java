
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
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
    @SingleRole
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
    @SingleRole
    void testFindOne_NotExisting() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne("abc");

        // WHILE
        Assertions.assertThat(role)
            .isEmpty();
    }

    @Test
    @DisplayName("When the role exists, and it has no granted permission, it is returned")
    @RoleWithCrudPermissionsNotGranted
    void testFindOne_WithNotGrantedPermission() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(role)
            .contains(Roles.withoutPermissions());
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
