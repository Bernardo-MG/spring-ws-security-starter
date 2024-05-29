
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ActionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ActionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.Action;
import com.bernardomg.security.authorization.permission.domain.repository.ActionRepository;
import com.bernardomg.security.authorization.permission.test.config.factory.ActionEntities;
import com.bernardomg.security.authorization.permission.test.config.factory.Actions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ActionRepository - save")
class ITActionRepositorySave {

    @Autowired
    private ActionSpringRepository actionSpringRepository;

    @Autowired
    private ActionRepository       repository;

    @Test
    @DisplayName("Persists the data")
    void testSave_Persisted() {
        final Iterable<ActionEntity> actions;
        final Action                 action;

        // GIVEN
        action = Actions.create();

        // WHEN
        repository.save(List.of(action));

        // THEN
        actions = actionSpringRepository.findAll();

        Assertions.assertThat(actions)
            .as("actions")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ActionEntities.create());
    }

    @Test
    @DisplayName("Returns the persisted data")
    void testSave_Returned() {
        final Collection<Action> created;
        final Action             action;

        // GIVEN
        action = Actions.create();

        // WHEN
        created = repository.save(List.of(action));

        // THEN
        Assertions.assertThat(created)
            .as("actions")
            .containsExactly(Actions.create());
    }

}
