
package com.bernardomg.security.role.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the role's name is not registered.
 */
public final class RoleNameNotExistsRule implements FieldRule<Role> {

    /**
     * Logger for the class.
     */
    private static final Logger  log = LoggerFactory.getLogger(RoleNameNotExistsRule.class);

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
