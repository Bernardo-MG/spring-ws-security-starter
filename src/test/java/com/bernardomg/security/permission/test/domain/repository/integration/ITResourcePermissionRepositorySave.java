
package com.bernardomg.security.permission.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.test.config.annotation.ResourceAndActions;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissionEntities;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - save")
class ITResourcePermissionRepositorySave {

    @Autowired
    private ResourcePermissionRepository       repository;

    @Autowired
    private ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    public ITResourcePermissionRepositorySave() {
        super();
    }

    @Test
    @DisplayName("Persists the data")
    @ResourceAndActions
    void testSave_Persisted() {
        final Iterable<ResourcePermissionEntity> permissions;
        final ResourcePermission                 permission;

        // GIVEN
        permission = ResourcePermissions.create();

        // WHEN
        repository.save(List.of(permission));

        // THEN
        permissions = resourcePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ResourcePermissionEntities.create());
    }

    @Test
    @DisplayName("Returns the persisted data")
    @ResourceAndActions
    void testSave_Returned() {
        final Collection<ResourcePermission> created;
        final ResourcePermission             permission;

        // GIVEN
        permission = ResourcePermissions.create();

        // WHEN
        created = repository.save(List.of(permission));

        // THEN
        Assertions.assertThat(created)
            .as("permissions")
            .containsExactly(ResourcePermissions.create());
    }

}
