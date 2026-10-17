
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithPermission;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.test.config.role.factory.RoleConstants;
import com.bernardomg.security.adapter.test.config.role.factory.Roles;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.repository.RoleRepository;

@IntegrationTest
@DisplayName("RoleRepository - find one")
class ITRoleRepositoryFindOne {

    @Autowired
    private RoleRepository repository;

    @Test
    @DisplayName("When the role exists, then it is returned")
    @RoleWithoutPermissions
    void testFindOne_Existing() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(role)
                .as("the role is present")
                .isPresent();
            role.ifPresent(actual -> softly.assertThat(actual)
                .as("the returned role matches")
                .usingRecursiveComparison()
                .ignoringFields("audit")
                .isEqualTo(Roles.withoutPermissions()));
        });
    }

    @Test
    @DisplayName("When there is no data, then nothing is returned")
    void testFindOne_NoData() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(role)
            .as("When there is no data, then nothing is returned (role)")
            .isEmpty();
    }

    @Test
    @DisplayName("When the role doesn't exist, then nothing is returned")
    @RoleWithoutPermissions
    void testFindOne_NotExisting() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne("abc");

        // THEN
        Assertions.assertThat(role)
            .as("When the role doesn't exist, then nothing is returned (role)")
            .isEmpty();
    }

    @Test
    @DisplayName("When the role has permissions, then it is returned")
    @RoleWithCrudPermissions
    void testFindOne_WithPermissions() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(role)
                .as("the role is present")
                .isPresent();
            role.ifPresent(actual -> softly.assertThat(actual)
                .as("the returned role includes its permissions")
                .usingRecursiveComparison()
                .ignoringFields("audit")
                .isEqualTo(Roles.withPermissions()));
        });
    }

    @Test
    @DisplayName("When the role has one permission, then it is returned")
    @RoleWithPermission
    void testFindOne_WithSinglePermission() {
        final Optional<Role> role;

        // WHEN
        role = repository.findOne(RoleConstants.NAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(role)
                .as("the role is present")
                .isPresent();
            role.ifPresent(actual -> softly.assertThat(actual)
                .as("the returned role includes its permission")
                .usingRecursiveComparison()
                .ignoringFields("audit")
                .isEqualTo(Roles.withSinglePermission()));
        });
    }

}
