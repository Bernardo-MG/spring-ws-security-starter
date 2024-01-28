
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - save")
class ITRoleRepositorySave {

    @Autowired
    private RoleRepository       repository;

    @Autowired
    private RoleSpringRepository springRepository;

    public ITRoleRepositorySave() {
        super();
    }

    @Test
    @DisplayName("Persists the data")
    void testSave_PersistedData() {
        final List<RoleEntity> roles;
        final RoleEntity       entity;
        final Role             role;

        // GIVEN
        role = Roles.valid();

        // WHEN
        repository.save(role);

        // THEN
        roles = springRepository.findAll();

        Assertions.assertThat(roles)
            .hasSize(1);

        entity = roles.iterator()
            .next();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entity.getId())
                .isNotNull();
            softly.assertThat(entity.getName())
                .isEqualTo(RoleConstants.NAME);
        });
    }

    @Test
    @DisplayName("Returns the created data")
    void testSave_ReturnedData() {
        final Role result;
        final Role role;

        // GIVEN
        role = Roles.valid();

        // WHEN
        result = repository.save(role);

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Roles.valid());
    }

}
