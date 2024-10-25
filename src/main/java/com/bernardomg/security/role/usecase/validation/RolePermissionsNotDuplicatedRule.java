
package com.bernardomg.security.role.usecase.validation;

import java.util.Optional;

import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the role's permission are not duplicated.
 */
@Slf4j
public final class RolePermissionsNotDuplicatedRule implements FieldRule<Role> {

    public RolePermissionsNotDuplicatedRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final Role role) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final long                   uniquePermissions;
        final int                    totalPermissions;
        final long                   duplicates;

        uniquePermissions = role.permissions()
            .stream()
            .map(ResourcePermission::getName)
            .distinct()
            .count();
        totalPermissions = role.permissions()
            .size();
        if (uniquePermissions < totalPermissions) {
            // TODO: is this really an error? It can be corrected easily
            duplicates = (totalPermissions - uniquePermissions);
            log.error("Received {} permissions, but {} are duplicates", totalPermissions, duplicates);
            fieldFailure = FieldFailure.of("permissions[]", "duplicated", duplicates);
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
