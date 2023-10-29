
package com.bernardomg.security.authorization.permission.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RemoveRolePermissionValidator implements Validator<RolePermission> {

    private final RolePermissionRepository rolePermissionRepository;

    public RemoveRolePermissionValidator(final RolePermissionRepository rolePermissionRepo) {
        super();

        rolePermissionRepository = Objects.requireNonNull(rolePermissionRepo);
    }

    @Override
    public final void validate(final RolePermission relationship) {
        final Collection<FieldFailure> failures;
        FieldFailure                   failure;

        failures = new ArrayList<>();

        // The role permission exists
        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(relationship.getRoleId(),
            relationship.getPermissionId())) {
            log.error("Found no role permission for role {} and permission {}", relationship.getRoleId(),
                relationship.getPermissionId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            // TODO: Use the correct id
            failure = FieldFailure.of("rolePermission", "notExisting", relationship.getRoleId());
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
