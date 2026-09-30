
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.permission;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.permission.ResourceEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.ResourceSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.permission.annotation.DataResource;
import com.bernardomg.security.adapter.test.config.permission.factory.ResourceEntities;
import com.bernardomg.security.adapter.test.config.permission.factory.Resources;
import com.bernardomg.security.domain.permission.model.Resource;
import com.bernardomg.security.domain.permission.repository.ResourceRepository;

@IntegrationTest
@DisplayName("ResourceRepository - save")
class ITResourceRepositorySave {

    @Autowired
    private ResourceRepository       repository;

    @Autowired
    private ResourceSpringRepository resourceSpringRepository;

    @Test
    @DisplayName("When no data is saved, then nothing is persisted")
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
    @DisplayName("When saving an existing resource, then the data is persisted")
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
    @DisplayName("When saving a resource, then the data is persisted")
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
    @DisplayName("When saving a resource, then the data is returned")
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
