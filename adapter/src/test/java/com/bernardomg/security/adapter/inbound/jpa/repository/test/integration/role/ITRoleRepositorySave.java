
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.role.RoleEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.role.RoleSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.permission.annotation.CrudPermissions;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.test.config.role.factory.RoleEntities;
import com.bernardomg.security.adapter.test.config.role.factory.Roles;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.repository.RoleRepository;

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
    @DisplayName("When saving an existing role with added permissions, then the role is updated")
    @RoleWithoutPermissions
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
            .as("role")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "permissions.id")
            .containsExactly(RoleEntities.withPermissions());
    }

    @Test
    @DisplayName("When saving an existing role with added permissions, then the updated role is returned")
    @RoleWithoutPermissions
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
            .as("role")
            .isEqualTo(Roles.withPermissions());
    }

    @Test
    @DisplayName("When saving an existing role with new permissions, then the role is updated")
    @RoleWithoutPermissions
    void testSave_AddPermissionsPermission_PersistedData() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withSinglePermission();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("role")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(RoleEntities.withoutPermissions());
    }

    @Test
    @DisplayName("When saving an existing role with new permissions, then the updated role is returned")
    @RoleWithoutPermissions
    void testSave_AddPermissionsPermission_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withSinglePermission();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("role")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When saving a role without permissions, then the role is persisted")
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
            .as("role")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "audit")
            .containsExactly(RoleEntities.withoutPermissions());
    }

    @Test
    @DisplayName("When saving a role, then the created role is returned")
    void testSave_NoPermissions_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("role")
            .usingRecursiveComparison()
            .ignoringFields("audit")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When saving a new role, then the role is persisted")
    @CrudPermissions
    void testSave_PersistedData() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withPermissions();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("role")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "permissions.id", "permissions.roleId",
                "audit")
            .containsExactly(RoleEntities.withPermissions());
    }

    @Test
    @DisplayName("When saving an existing role with removed permissions, then the role is updated")
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
            .as("role")
            .hasSize(1)
            .first()
            .extracting(RoleEntity::getPermissions)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .isEmpty();
    }

    @Test
    @DisplayName("When saving an existing role with removed permissions, then the updated role is returned")
    @RoleWithCrudPermissions
    void testSave_RemovePermissions_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("role")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When saving a new role, then the created role is returned")
    @CrudPermissions
    void testSave_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("role")
            .usingRecursiveComparison()
            .ignoringFields("audit")
            .isEqualTo(Roles.withPermissions());
    }

    @Test
    @DisplayName("When saving a role without permissions, then the role is persisted")
    @CrudPermissions
    void testSave_WithoutPermissions_PersistedData() {
        final List<RoleEntity> roles;
        final Role             role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .as("role")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "permissions.roleId", "audit")
            .containsExactly(RoleEntities.withoutPermissions());
    }

    @Test
    @DisplayName("When saving a role without permissions, then the role is returned")
    @CrudPermissions
    void testSave_WithoutPermissions_ReturnedData() {
        final Role saved;
        final Role role;

        // GIVEN
        role = Roles.withoutPermissions();

        // WHEN
        saved = repository.save(role);

        // THEN
        Assertions.assertThat(saved)
            .as("role")
            .usingRecursiveComparison()
            .ignoringFields("audit")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When saving a role with permissions, then the role is persisted")
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
            .as("role")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "permissions.roleId", "permissions.id",
                "audit")
            .containsExactly(RoleEntities.withPermissions());
    }

    @Test
    @DisplayName("When saving a role with permissions, then the role is returned")
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
            .as("role")
            .usingRecursiveComparison()
            .ignoringFields("audit")
            .isEqualTo(Roles.withPermissions());
    }

}
