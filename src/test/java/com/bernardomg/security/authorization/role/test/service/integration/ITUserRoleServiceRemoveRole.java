
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.test.config.UserWithPermission;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.UserRole;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.util.model.UserRoles;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - remove role")
@UserWithPermission
class ITUserRoleServiceRemoveRole {

    @Autowired
    private UserRoleService    service;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public ITUserRoleServiceRemoveRole() {
        super();
    }

    @Test
    @DisplayName("Reading the roles after removing a role doesn't return it")
    void testRemoveRole_CallBack() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        service.removeRole(1L, 1L);
        roles = service.getRoles(1L, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("Removes the entity when removing a role")
    void testRemoveRole_RemovesEntity() {
        service.removeRole(1L, 1L);

        Assertions.assertThat(userRoleRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemoveRole_ReturnedData() {
        final UserRole role;

        role = service.removeRole(1L, 1L);

        Assertions.assertThat(role)
            .isEqualTo(UserRoles.valid());
    }

}
