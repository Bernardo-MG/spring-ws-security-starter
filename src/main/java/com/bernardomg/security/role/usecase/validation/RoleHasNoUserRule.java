
package com.bernardomg.security.role.usecase.validation;

import java.util.Objects;
import java.util.Optional;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the role is not linked to a user.
 * <p>
 * TODO: instead of this, use on delete cascade
 */
@Slf4j
public final class RoleHasNoUserRule implements FieldRule<Role> {

    /**
     * Role repository.
     */
    private final RoleRepository roleRepository;

    public RoleHasNoUserRule(final RoleRepository roleRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);
    }

    @Override
    public final Optional<FieldFailure> check(final Role role) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (roleRepository.isLinkedToUser(role.name())) {
            log.error("Role with id {} has a relationship with a user", role);
            // TODO: Is the code exists or is it existing? Make sure all use the same
            fieldFailure = FieldFailure.of("user", "existing", role);
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
