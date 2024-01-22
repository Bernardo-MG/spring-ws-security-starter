
package com.bernardomg.security.authorization.role.test.service.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.test.config.factory.UserRoleEntities;
import com.bernardomg.security.authorization.role.usecase.service.UserRoleService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - add role")
class ITUserRoleServiceAddRole {

    @Autowired
    private UserRoleService    service;

    @Autowired
    private UserRoleSpringRepository userRoleRepository;

    public ITUserRoleServiceAddRole() {
        super();
    }

    @Test
    @DisplayName("Adds an entity when adding a role")
    @RoleWithPermission
    @OnlyUser
    void testAddRole_AddsEntity() {
        final List<UserRoleEntity> userRoles;

        service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        userRoles = userRoleRepository.findAll();

        Assertions.assertThat(userRoles)
            .containsExactly(UserRoleEntities.valid());
    }

    @Test
    @DisplayName("Adding an existing role adds nothing")
    @ValidUser
    void testAddRole_Existing() {
        final List<UserRoleEntity> userRoles;

        service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        userRoles = userRoleRepository.findAll();

        Assertions.assertThat(userRoles)
            .containsExactly(UserRoleEntities.valid());
    }

    @Test
    @DisplayName("Adding a new role persists the data")
    @ValidUser
    void testAddRole_Persists() {
        final List<UserRoleEntity> userRoles;

        service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        userRoles = userRoleRepository.findAll();

        Assertions.assertThat(userRoles)
            .containsExactly(UserRoleEntities.valid());
    }

    @Test
    @DisplayName("Returns the created data")
    @ValidUser
    void testAddRole_ReturnedData() {
        final Role userRole;

        userRole = service.addRole(UserConstants.USERNAME, RoleConstants.NAME);

        Assertions.assertThat(userRole)
            .isEqualTo(Roles.valid());
    }

}
