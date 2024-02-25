
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.AlternativeRole;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.UserRoleEntities;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRoleRepository - save")
class ITUserRoleRepositorySave {

    @Autowired
    private UserRoleRepository       repository;

    @Autowired
    private UserRoleSpringRepository springRepository;

    public ITUserRoleRepositorySave() {
        super();
    }

    @Test
    @DisplayName("Adding a role which the user already has adds nothing")
    @UserWithPermission
    void testSave_AddExisting() {
        final List<UserRoleEntity> roles;

        // WHEN
        repository.save(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .containsExactly(UserRoleEntities.valid());
    }

    @Test
    @DisplayName("Adding a new role when the user already has one persists it")
    @UserWithPermission
    @AlternativeRole
    void testSave_AddNew_CallBack() {
        final List<UserRoleEntity> roles;

        // WHEN
        repository.save(UserConstants.USERNAME, RoleConstants.ALTERNATIVE_NAME);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .containsExactly(UserRoleEntities.valid(), UserRoleEntities.alternative());
    }

    @Test
    @DisplayName("Persists the data")
    @RoleWithPermission
    @OnlyUser
    void testSave_PersistedData() {
        final List<UserRoleEntity> roles;

        // WHEN
        repository.save(UserConstants.USERNAME, RoleConstants.NAME);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .containsExactly(UserRoleEntities.valid());
    }

}
