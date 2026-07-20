
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.permission;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.permission.ActionEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.ActionSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.CreateAction;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.factory.ActionEntities;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.factory.Actions;
import com.bernardomg.security.domain.permission.model.Action;
import com.bernardomg.security.domain.permission.repository.ActionRepository;

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
