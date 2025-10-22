
package com.bernardomg.security.permission.test.domain.repository.integration;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.test.config.annotation.SinglePermission;
import com.bernardomg.security.permission.test.config.factory.PermissionConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - find all names")
class ITResourcePermissionRepositoryFindAllNames {

    @Autowired
    private ResourcePermissionRepository repository;

    @Test
    @DisplayName("When there is a permission its name is returned")
    @SinglePermission
    void testFindAllNames() {
        final Collection<String> names;

        // WHEN
        names = repository.findAllNames();

        // THEN
        Assertions.assertThat(names)
            .as("permission names")
            .containsOnly(PermissionConstants.DATA_CREATE);
    }

    @Test
    @DisplayName("When there is no data an empty list is returned")
    void testFindAllNames_NoData() {
        final Collection<String> names;

        // WHEN
        names = repository.findAllNames();

        // THEN
        Assertions.assertThat(names)
            .as("permission names")
            .isEmpty();
    }

}
