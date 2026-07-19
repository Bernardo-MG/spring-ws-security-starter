
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.permission;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.DataResource;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.factory.PermissionConstants;
import com.bernardomg.security.domain.permission.repository.ResourceRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourceRepository - find all names")
class ITResourceRepositoryFindAllNames {

    @Autowired
    private ResourceRepository repository;

    @Test
    @DisplayName("When there is an resource its name is returned")
    @DataResource
    void testFindAllNames() {
        final Collection<String> names;

        // WHEN
        names = repository.findAllNames();

        // THEN
        Assertions.assertThat(names)
            .as("resource names")
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
            .as("resource names")
            .isEmpty();
    }

}
