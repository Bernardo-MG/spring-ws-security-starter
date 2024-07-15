
package com.bernardomg.security.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.domain.repository.ResourceRepository;
import com.bernardomg.security.permission.test.config.annotation.SingleResource;
import com.bernardomg.security.permission.test.config.factory.PermissionConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourceRepository - exists")
class ITResourceRepositoryExists {

    @Autowired
    private ResourceRepository repository;

    @Test
    @DisplayName("When the permission exists it is returned as such")
    @SingleResource
    void testExists() {
        final boolean exists;

        // WHEN
        exists = repository.exists(PermissionConstants.DATA);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When there is no data it is returned as not existing")
    void testExists_noData() {
        final boolean exists;

        // WHEN
        exists = repository.exists(PermissionConstants.DATA);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

}
