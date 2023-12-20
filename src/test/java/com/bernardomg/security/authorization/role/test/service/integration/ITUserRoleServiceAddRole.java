
package com.bernardomg.security.authorization.role.test.service.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authorization.permission.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.persistence.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.security.authorization.role.test.util.model.UserRoleEntities;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - add role")
class ITUserRoleServiceAddRole {

    @Autowired
    private UserRoleService    service;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public ITUserRoleServiceAddRole() {
        super();
    }

    @Test
    @DisplayName("Adds an entity when adding a role")
    @RoleWithPermission
    @OnlyUser
    void testAddRole_AddsEntity() {
        final List<UserRoleEntity> userRoles;

        service.addRole(1L, 1L);

        userRoles = userRoleRepository.findAll();

        Assertions.assertThat(userRoles)
            .containsExactly(UserRoleEntities.valid());
    }

    @Test
    @DisplayName("Reading the roles after adding a role returns the new role")
    @ValidUser
    void testAddRole_CallBack() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        service.addRole(1L, 1L);
        roles = service.getRoles(1L, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("Adding an existing role adds nothing")
    @ValidUser
    void testAddRole_Existing() {
        final List<UserRoleEntity> userRoles;

        service.addRole(1L, 1L);

        userRoles = userRoleRepository.findAll();

        Assertions.assertThat(userRoles)
            .containsExactly(UserRoleEntities.valid());
    }

    @Test
    @DisplayName("Returns the created data")
    @ValidUser
    void testAddRole_ReturnedData() {
        final Role userRole;

        userRole = service.addRole(1L, 1L);

        Assertions.assertThat(userRole)
            .isEqualTo(Roles.valid());
    }

}
