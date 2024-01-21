
package com.bernardomg.security.authorization.role.test.service.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.RoleService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role service - create")
class ITRoleServiceCreate {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleService    service;

    public ITRoleServiceCreate() {
        super();
    }

    @Test
    @DisplayName("Persists the data")
    void testCreate_PersistedData() {
        final List<RoleEntity> roles;
        final RoleEntity       role;

        service.create(RoleConstants.NAME);
        roles = repository.findAll();

        Assertions.assertThat(roles)
            .hasSize(1);

        role = roles.iterator()
            .next();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(role.getId())
                .isNotNull();
            softly.assertThat(role.getName())
                .isEqualTo(RoleConstants.NAME);
        });
    }

    @Test
    @DisplayName("Returns the created data")
    void testCreate_ReturnedData() {
        final Role result;

        result = service.create(RoleConstants.NAME);

        Assertions.assertThat(result)
            .isEqualTo(Roles.valid());
    }

}
