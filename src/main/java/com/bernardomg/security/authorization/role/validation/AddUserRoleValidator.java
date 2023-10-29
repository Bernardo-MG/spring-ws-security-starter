
package com.bernardomg.security.authorization.role.validation;

import java.util.ArrayList;
import java.util.Collection;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authorization.role.model.UserRole;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AddUserRoleValidator implements Validator<UserRole> {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public AddUserRoleValidator(final UserRepository userRepo, final RoleRepository roleRepo) {
        super();

        userRepository = userRepo;
        roleRepository = roleRepo;
    }

    @Override
    public final void validate(final UserRole relationship) {
        final Collection<FieldFailure> failures;
        FieldFailure                   failure;

        failures = new ArrayList<>();

        // The user exists
        if (!userRepository.existsById(relationship.getUserId())) {
            log.error("Found no user with id {}", relationship.getUserId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            failure = FieldFailure.of("id", "notExisting", relationship.getUserId());
            failures.add(failure);
        }

        // The action exists
        if (!roleRepository.existsById(relationship.getRoleId())) {
            log.error("Found no role with id {}", relationship.getRoleId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            failure = FieldFailure.of("role", "notExisting", relationship.getRoleId());
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
