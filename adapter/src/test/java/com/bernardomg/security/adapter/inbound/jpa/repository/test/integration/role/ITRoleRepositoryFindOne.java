
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.annotation.RoleWithPermission;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.factory.RoleConstants;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.role.factory.Roles;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.repository.RoleRepository;

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
