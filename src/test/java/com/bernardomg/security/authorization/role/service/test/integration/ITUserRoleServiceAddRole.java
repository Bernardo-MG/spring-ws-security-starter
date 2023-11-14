
package com.bernardomg.security.authorization.role.service.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.UserRole;
import com.bernardomg.security.authorization.role.persistence.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.service.UserRoleService;
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
    @ValidUser
    void testAddRole_AddsEntity() {
        final UserRoleEntity entity;

        service.addRole(1L, 1L);

        Assertions.assertThat(userRoleRepository.count())
            .isEqualTo(1);

        entity = userRoleRepository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(entity.getUserId())
            .isEqualTo(1);
        Assertions.assertThat(entity.getRoleId())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Reading the roles after adding a role returns the new role")
    @ValidUser
    void testAddRole_CallBack() {
        final Iterable<Role> result;
        final Role           role;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        service.addRole(1L, 1L);
        result = service.getRoles(1L, pageable);

        Assertions.assertThat(result)
            .hasSize(1);

        role = result.iterator()
            .next();

        Assertions.assertThat(role.getName())
            .isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Adding an existing role adds nothing")
    @ValidUser
    void testAddRole_Existing() {
        service.addRole(1L, 1L);

        Assertions.assertThat(userRoleRepository.count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Returns the created data")
    @ValidUser
    void testAddRole_ReturnedData() {
        final UserRole entity;

        entity = service.addRole(1L, 1L);

        Assertions.assertThat(entity.getUserId())
            .isEqualTo(1);
        Assertions.assertThat(entity.getRoleId())
            .isEqualTo(1);
    }

}
