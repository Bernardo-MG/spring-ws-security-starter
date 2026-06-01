
package com.bernardomg.security.permission.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ActionEntity;
import com.bernardomg.security.permission.adapter.inbound.jpa.repository.ActionSpringRepository;
import com.bernardomg.security.permission.domain.model.Action;
import com.bernardomg.security.permission.domain.repository.ActionRepository;
import com.bernardomg.security.permission.test.config.annotation.CreateAction;
import com.bernardomg.security.permission.test.config.factory.ActionEntities;
import com.bernardomg.security.permission.test.config.factory.Actions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ActionRepository - save all")
class ITActionRepositorySave {

    @Autowired
    private ActionSpringRepository actionSpringRepository;

    @Autowired
    private ActionRepository       repository;

    @Test
    @DisplayName("When saving no data nothing is persisted")
    void testSaveAll_Empty() {
        final Iterable<ActionEntity> actions;

        // WHEN
        repository.saveAll(List.of());

        // THEN
        actions = actionSpringRepository.findAll();

        Assertions.assertThat(actions)
            .as("actions")
            .isEmpty();
    }

    @Test
    @DisplayName("When saving an action that already exists, the data is persisted")
    @CreateAction
    void testSaveAll_Existing_Persisted() {
        final Iterable<ActionEntity> actions;
        final Action                 action;

        // GIVEN
        action = Actions.create();

        // WHEN
        repository.saveAll(List.of(action));

        // THEN
        actions = actionSpringRepository.findAll();

        Assertions.assertThat(actions)
            .as("actions")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ActionEntities.create());
    }

    @Test
    @DisplayName("When saving an action the data is persisted")
    void testSaveAll_Persisted() {
        final Iterable<ActionEntity> actions;
        final Action                 action;

        // GIVEN
        action = Actions.create();

        // WHEN
        repository.saveAll(List.of(action));

        // THEN
        actions = actionSpringRepository.findAll();

        Assertions.assertThat(actions)
            .as("actions")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(ActionEntities.create());
    }

    @Test
    @DisplayName("When saving an action the data is returned")
    void testSaveAll_Returned() {
        final Collection<Action> created;
        final Action             action;

        // GIVEN
        action = Actions.create();

        // WHEN
        created = repository.saveAll(List.of(action));

        // THEN
        Assertions.assertThat(created)
            .as("actions")
            .containsExactly(Actions.create());
    }

}
