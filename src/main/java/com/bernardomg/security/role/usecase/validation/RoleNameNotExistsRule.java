
package com.bernardomg.security.role.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the role's name is not registered.
 */
@Slf4j
public final class RoleNameNotExistsRule implements FieldRule<Role> {

    /**
     * Role repository.
     */
    private final RoleRepository roleRepository;

    public RoleNameNotExistsRule(final RoleRepository roleRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);
    }

    @Override
    public final Optional<FieldFailure> check(final Role role) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (roleRepository.exists(role.name())) {
            log.error("A role already exists with the name {}", role.name());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            fieldFailure = new FieldFailure("existing", "name", role.name());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
