
package com.bernardomg.security.user.data.usecase.validation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the user's roles are not duplicated.
 */
public final class UserRolesNotDuplicatedRule implements FieldRule<User> {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserRolesNotDuplicatedRule.class);

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

        uniqueRoles = user.roles()
            .stream()
            .map(Role::name)
            .distinct()
            .count();
        totalRoles = user.roles()
            .size();
        if (uniqueRoles < totalRoles) {
            // TODO: is this really an error? It can be corrected easily
            duplicates = (totalRoles - uniqueRoles);
            log.error("Received {} roles, but {} are duplicates", totalRoles, duplicates);
            fieldFailure = new FieldFailure("duplicated", "roles[]", duplicates);
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
