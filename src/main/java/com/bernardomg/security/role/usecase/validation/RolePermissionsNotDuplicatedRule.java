
package com.bernardomg.security.role.usecase.validation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the role's permission are not duplicated.
 */
public final class RolePermissionsNotDuplicatedRule implements FieldRule<Role> {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(RolePermissionsNotDuplicatedRule.class);

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
            .map(p -> String.format("%s:%s", p.resource(), p.action()))
            .distinct()
            .count();
        totalPermissions = role.permissions()
            .size();
        if (uniquePermissions < totalPermissions) {
            // TODO: is this really an error? It can be corrected easily
            duplicates = (totalPermissions - uniquePermissions);
            log.error("Received {} permissions, but {} are duplicates", totalPermissions, duplicates);
            fieldFailure = new FieldFailure("duplicated", "permissions[]", duplicates);
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
