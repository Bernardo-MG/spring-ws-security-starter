
package com.bernardomg.security.authorization.role.test.service.integration;

import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.test.config.UserWithPermission;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.config.AlternativeRole;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - add role - with role")
@UserWithPermission
@AlternativeRole
class ITUserRoleServiceAddRoleWithRoles {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceAddRoleWithRoles() {
        super();
    }

    @Test
    @DisplayName("Adding a role which the user already has adds nothing")
    void testAddRoles_AddExisting_CallBack() {
        final Iterable<Role>     roles;
        final Collection<String> roleNames;
        final Pageable           pageable;

        pageable = Pageable.unpaged();

        service.addRole(1l, 2l);
        roles = service.getRoles(1l, pageable);

        // FIXME: Should be a single role
        Assertions.assertThat(roles)
            .hasSize(2);

        roleNames = StreamSupport.stream(roles.spliterator(), false)
            .map(Role::getName)
            .toList();

        Assertions.assertThat(roleNames)
            .contains(Roles.NAME);
    }

    @Test
    @DisplayName("Reading the roles after adding a new role returns them")
    void testAddRoles_AddNew_CallBack() {
        final Iterable<Role>     roles;
        final Collection<String> roleNames;
        final Pageable           pageable;

        pageable = Pageable.unpaged();

        service.addRole(1l, 2l);
        roles = service.getRoles(1l, pageable);

        // FIXME: Should be a single role
        Assertions.assertThat(roles)
            .hasSize(2);

        roleNames = StreamSupport.stream(roles.spliterator(), false)
            .map(Role::getName)
            .toList();

        Assertions.assertThat(roleNames)
            .containsAll(List.of(Roles.NAME, Roles.ALTERNATIVE_NAME));
    }

}
