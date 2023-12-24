
package com.bernardomg.security.authorization.role.test.service.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.permission.test.config.UserWithPermission;
import com.bernardomg.security.authorization.role.persistence.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.config.AlternativeRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.test.config.factory.UserRoleEntities;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - add role - with role")
class ITUserRoleServiceAddRoleWithRoles {

    @Autowired
    private UserRoleService    service;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public ITUserRoleServiceAddRoleWithRoles() {
        super();
    }

    @Test
    @DisplayName("Adding a role which the user already has adds nothing")
    @UserWithPermission
    void testAddRoles_AddExisting_CallBack() {
        final List<UserRoleEntity> userRoles;

        service.addRole(Users.USERNAME, Roles.NAME);

        userRoles = userRoleRepository.findAll();

        Assertions.assertThat(userRoles)
            .containsExactly(UserRoleEntities.valid());
    }

    @Test
    @DisplayName("Adding a new role persists it")
    @UserWithPermission
    @AlternativeRole
    void testAddRoles_AddNew_CallBack() {
        final List<UserRoleEntity> userRoles;

        service.addRole(Users.USERNAME, Roles.ALTERNATIVE_NAME);

        userRoles = userRoleRepository.findAll();

        Assertions.assertThat(userRoles)
            .containsExactly(UserRoleEntities.valid(), UserRoleEntities.alternative());
    }

}
