
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourceEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourceSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.Resource;
import com.bernardomg.security.authorization.permission.domain.repository.ResourceRepository;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourceEntities;
import com.bernardomg.security.authorization.permission.test.config.factory.Resources;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - save")
class ITResourceRepositorySave {

    @Autowired
    private ResourceRepository       repository;

    @Autowired
    private ResourceSpringRepository resourceSpringRepository;

    @Test
    @DisplayName("Persists the data")
    void testRemovePermission_Persisted() {
        final Iterable<ResourceEntity> permissions;
        final Resource                 permission;

        // GIVEN
        permission = Resources.data();

        // WHEN
        repository.save(List.of(permission));

        // THEN
        permissions = resourceSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("resources")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ResourceEntities.data());
    }

    @Test
    @DisplayName("Returns the persisted data")
    void testRemovePermission_Returned() {
        final Collection<Resource> created;
        final Resource             permission;

        // GIVEN
        permission = Resources.data();

        // WHEN
        created = repository.save(List.of(permission));

        // THEN
        Assertions.assertThat(created)
            .as("resource")
            .containsExactly(Resources.data());
    }

}
