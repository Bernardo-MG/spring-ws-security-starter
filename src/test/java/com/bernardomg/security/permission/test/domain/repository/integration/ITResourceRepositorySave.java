
package com.bernardomg.security.permission.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourceEntity;
import com.bernardomg.security.permission.adapter.inbound.jpa.repository.ResourceSpringRepository;
import com.bernardomg.security.permission.domain.model.Resource;
import com.bernardomg.security.permission.domain.repository.ResourceRepository;
import com.bernardomg.security.permission.test.config.annotation.DataResource;
import com.bernardomg.security.permission.test.config.factory.ResourceEntities;
import com.bernardomg.security.permission.test.config.factory.Resources;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - save")
class ITResourceRepositorySave {

    @Autowired
    private ResourceRepository       repository;

    @Autowired
    private ResourceSpringRepository resourceSpringRepository;

    @Test
    @DisplayName("When saving no data nothing is persisted")
    void testSaveAll_Empty() {
        final Iterable<ResourceEntity> permissions;

        // WHEN
        repository.saveAll(List.of());

        // THEN
        permissions = resourceSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("resources")
            .isEmpty();
    }

    @Test
    @DisplayName("When saving an existing resource, the data is persisted")
    @DataResource
    void testSaveAll_Existing_Persisted() {
        final Iterable<ResourceEntity> permissions;
        final Resource                 permission;

        // GIVEN
        permission = Resources.data();

        // WHEN
        repository.saveAll(List.of(permission));

        // THEN
        permissions = resourceSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("resources")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ResourceEntities.data());
    }

    @Test
    @DisplayName("When saving a resource the data is persisted")
    void testSaveAll_Persisted() {
        final Iterable<ResourceEntity> permissions;
        final Resource                 permission;

        // GIVEN
        permission = Resources.data();

        // WHEN
        repository.saveAll(List.of(permission));

        // THEN
        permissions = resourceSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("resources")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ResourceEntities.data());
    }

    @Test
    @DisplayName("When saving a resource the data is returned")
    void testSaveAll_Returned() {
        final Collection<Resource> created;
        final Resource             permission;

        // GIVEN
        permission = Resources.data();

        // WHEN
        created = repository.saveAll(List.of(permission));

        // THEN
        Assertions.assertThat(created)
            .as("resource")
            .containsExactly(Resources.data());
    }

}
