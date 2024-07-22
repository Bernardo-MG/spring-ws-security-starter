
package com.bernardomg.security.user.data.usecase.validation;

import java.util.Optional;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

import lombok.extern.slf4j.Slf4j;

/**
 * Checks the user's roles are not duplicated.
 */
@Slf4j
public final class UserRolesNotDuplicatedRule implements FieldRule<User> {

    public UserRolesNotDuplicatedRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final User user) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final long                   uniqueRoles;
        final int                    totalRoles;
        final long                   duplicates;

        uniqueRoles = user.getRoles()
            .stream()
            .map(Role::getName)
            .distinct()
            .count();
        totalRoles = user.getRoles()
            .size();
        if (uniqueRoles < totalRoles) {
            // TODO: is this really an error? It can be corrected easily
            duplicates = (totalRoles - uniqueRoles);
            log.error("Received {} roles, but {} are duplicates", totalRoles, duplicates);
            fieldFailure = FieldFailure.of("roles[]", "duplicated", duplicates);
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
