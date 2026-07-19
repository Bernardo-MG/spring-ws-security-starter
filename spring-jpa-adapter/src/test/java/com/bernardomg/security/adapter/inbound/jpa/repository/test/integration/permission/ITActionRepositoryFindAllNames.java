
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.permission;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.CreateAction;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.factory.PermissionConstants;
import com.bernardomg.security.domain.permission.repository.ActionRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ActionRepository - find all names")
class ITActionRepositoryFindAllNames {

    @Autowired
    private ActionRepository repository;

    @Test
    @DisplayName("When there is an action its name is returned")
    @CreateAction
    void testFindAllNames() {
        final Collection<String> names;

        // WHEN
        names = repository.findAllNames();

        // THEN
        Assertions.assertThat(names)
            .as("action names")
            .containsOnly(PermissionConstants.CREATE);
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
