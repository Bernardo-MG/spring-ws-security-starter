
package com.bernardomg.security.authorization.role.test.service.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.service.RoleService;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role service - get one")
@SingleRole
class ITRoleServiceGetOne {

    @Autowired
    private RoleService service;

    public ITRoleServiceGetOne() {
        super();
    }

    @Test
    @DisplayName("Returns the correct data when reading a single entity")
    void testGetOne_Existing_Data() {
        final Optional<Role> role;

        role = service.getOne(Roles.NAME);

        Assertions.assertThat(role)
            .contains(Roles.valid());
    }

}
