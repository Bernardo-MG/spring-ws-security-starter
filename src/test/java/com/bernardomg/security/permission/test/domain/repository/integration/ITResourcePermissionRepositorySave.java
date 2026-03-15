
package com.bernardomg.security.permission.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.test.config.annotation.ResourceAndActions;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissionEntities;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - save all")
class ITResourcePermissionRepositorySave {

    @Autowired
    private ResourcePermissionRepository       repository;

    @Autowired
    private ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    public ITResourcePermissionRepositorySave() {
        super();
    }

    @Test
    @DisplayName("When saving no data nothing is persisted")
    @ResourceAndActions
    void testSaveAll_Empty() {
        final Iterable<ResourcePermissionEntity> permissions;

        // WHEN
        repository.saveAll(List.of());

        // THEN
        permissions = resourcePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When saving a resource the data is persisted")
    @ResourceAndActions
    void testSaveAll_Persisted() {
        final Iterable<ResourcePermissionEntity> permissions;
        final ResourcePermission                 permission;

        // GIVEN
        permission = ResourcePermissions.create();

        // WHEN
        repository.saveAll(List.of(permission));

        // THEN
        permissions = resourcePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ResourcePermissionEntities.create());
    }

    @Test
    @DisplayName("When saving a resource the data is returned")
    @ResourceAndActions
    void testSaveAll_Returned() {
        final Collection<ResourcePermission> created;
        final ResourcePermission             permission;

        // GIVEN
        permission = ResourcePermissions.create();

        // WHEN
        created = repository.saveAll(List.of(permission));

        // THEN
        Assertions.assertThat(created)
            .as("permissions")
            .containsExactly(ResourcePermissions.create());
    }

}
