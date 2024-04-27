
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleEntities;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - save")
class ITRoleRepositorySave {

    @Autowired
    private RoleRepository       repository;

    @Autowired
    private RoleSpringRepository springRepository;

    public ITRoleRepositorySave() {
        super();
    }

    @Test
    @DisplayName("Updates an existing role adding permissions")
    @SingleRole
    void testSave_AddPermissions_NotExistingPermission() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withSinglePermission();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("roles")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(RoleEntities.withoutPermissions());
    }

    @Test
    @DisplayName("Updates an existing role adding permissions")
    @SingleRole
    @CrudPermissions
    void testSave_AddPermissions_PersistedData() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withPermissions();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("roles")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(RoleEntities.withPermissions());
    }

    @Test
    @DisplayName("Returns an existing role adding permissions")
    @SingleRole
    @CrudPermissions
    void testSave_AddPermissions_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("roles")
            .isEqualTo(Roles.withPermissions());
    }

    @Test
    @DisplayName("Persists a role with no permissions")
    void testSave_NoPermissions_PersistedData() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("roles")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(RoleEntities.withoutPermissions());
    }

    @Test
    @DisplayName("Returns the created data")
    void testSave_NoPermissions_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("roles")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Updates an existing role removing permissions")
    @RoleWithCrudPermissions
    void testSave_RemovePermissions_PersistedData() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("roles")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(RoleEntities.withoutPermissions());
    }

    @Test
    @DisplayName("Returns an existing role removing permissions")
    @RoleWithCrudPermissions
    void testSave_RemovePermissions_ReturneddData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("roles")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Persists a role with permissions")
    @CrudPermissions
    void testSave_WithPermissions_PersistedData() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withPermissions();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("roles")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "permissions.id.roleId")
            .containsExactly(RoleEntities.withPermissions());
    }

    @Test
    @DisplayName("Returns a role with permissions")
    @CrudPermissions
    void testSave_WithPermissions_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("roles")
            .isEqualTo(Roles.withPermissions());
    }

}
