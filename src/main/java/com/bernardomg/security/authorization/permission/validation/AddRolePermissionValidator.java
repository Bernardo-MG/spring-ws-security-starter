
package com.bernardomg.security.authorization.permission.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.bernardomg.security.authorization.permission.persistence.repository.PermissionRepository;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AddRolePermissionValidator implements Validator<RolePermission> {

    private final PermissionRepository rolePermissionRepository;

    private final RoleRepository       roleRepository;

    public AddRolePermissionValidator(final RoleRepository roleRepo, final PermissionRepository rolePermissionRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);
        rolePermissionRepository = Objects.requireNonNull(rolePermissionRepo);
    }

    @Override
    public final void validate(final RolePermission relationship) {
        final Collection<FieldFailure> failures;
        FieldFailure                   failure;

        failures = new ArrayList<>();

        // The role exists
        if (!roleRepository.existsById(relationship.getRoleId())) {
            log.error("Found no role with id {}", relationship.getRoleId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            failure = FieldFailure.of("role", "notExisting", relationship.getRoleId());
            failures.add(failure);
        }

        // The permission exists
        if (!rolePermissionRepository.existsById(relationship.getPermissionId())) {
            log.error("Found no permission with id {}", relationship.getPermissionId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            failure = FieldFailure.of("permission", "notExisting", relationship.getPermissionId());
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
