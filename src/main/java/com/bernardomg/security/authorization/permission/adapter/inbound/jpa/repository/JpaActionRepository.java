
package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Optional;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ActionEntity;
import com.bernardomg.security.authorization.permission.domain.model.Action;
import com.bernardomg.security.authorization.permission.domain.repository.ActionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JpaActionRepository implements ActionRepository {

    private final ActionSpringRepository actionRepository;

    public JpaActionRepository(final ActionSpringRepository actionRepo) {
        super();

        actionRepository = actionRepo;
    }

    @Override
    public final boolean exists(final String name) {
        return actionRepository.existsByName(name);
    }

    @Override
    public final Action save(final Action action) {
        final Optional<ActionEntity> existing;
        final ActionEntity           entity;
        final ActionEntity           created;

        log.debug("Saving action {}", action);

        entity = toEntity(action);

        existing = actionRepository.findByName(action.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        created = actionRepository.save(entity);

        return toDomain(created);
    }

    private final Action toDomain(final ActionEntity action) {
        return Action.builder()
            .withName(action.getName())
            .build();
    }

    private final ActionEntity toEntity(final Action action) {
        return ActionEntity.builder()
            .withName(action.getName())
            .build();
    }

}
