
package com.bernardomg.security.permission.test.domain.repository.integration;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.data.domain.repository.ResourceRepository;
import com.bernardomg.security.permission.test.config.annotation.SingleResource;
import com.bernardomg.security.permission.test.config.factory.PermissionConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourceRepository - find all names")
class ITResourceRepositoryFindAllNames {

    @Autowired
    private ResourceRepository repository;

    @Test
    @DisplayName("When there is an resource its name is returned")
    @SingleResource
    void testFindAllNames() {
        final Collection<String> names;

        // WHEN
        names = repository.findAllNames();

        // THEN
        Assertions.assertThat(names)
            .as("action names")
            .containsOnly(PermissionConstants.DATA);
    }

    @Test
    @DisplayName("When there is no data an empty list is returned")
    void testFindAllNames_NoData() {
        final Collection<String> names;

        // WHEN
        names = repository.findAllNames();

        // THEN
        Assertions.assertThat(names)
            .as("action names")
            .isEmpty();
    }

}
