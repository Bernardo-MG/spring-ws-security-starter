
package com.bernardomg.security.authorization.role.validation;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.domain.Example;

import com.bernardomg.security.authorization.role.persistence.model.PersistentUserRole;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DeleteRoleValidator implements Validator<Long> {

    private final RoleRepository     roleRepository;

    private final UserRoleRepository userRoleRepository;

    public DeleteRoleValidator(final RoleRepository roleRepo, final UserRoleRepository userRoleRepo) {
        super();

        roleRepository = roleRepo;
        userRoleRepository = userRoleRepo;
    }

    @Override
    public final void validate(final Long id) {
        final Collection<FieldFailure> failures;
        FieldFailure                   failure;
        final PersistentUserRole       sample;

        failures = new ArrayList<>();

        // The role exists
        if (!roleRepository.existsById(id)) {
            log.error("Found no role with id {}", id);
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            failure = FieldFailure.of("id", "notExisting", id);
            failures.add(failure);
        }

        sample = PersistentUserRole.builder()
            .roleId(id)
            .build();

        // No user has the role
        if (userRoleRepository.exists(Example.of(sample))) {
            log.error("Role with id {} has a relationship with a user", id);
            // TODO: Is the code exists or is it existing? Make sure all use the same
            failure = FieldFailure.of("user", "existing", id);
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
