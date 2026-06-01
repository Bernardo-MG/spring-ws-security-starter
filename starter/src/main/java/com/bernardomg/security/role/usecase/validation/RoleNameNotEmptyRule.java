
package com.bernardomg.security.role.usecase.validation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the role's name is not registered.
 */
public final class RoleNameNotEmptyRule implements FieldRule<Role> {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(RoleNameNotEmptyRule.class);

    public RoleNameNotEmptyRule() {
        super();
    }

    @Override
    public final Optional<FieldFailure> check(final Role role) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;

        if (ObjectUtils.isEmpty(role.name())) {
            log.error("Received an empty name for the role");
            fieldFailure = new FieldFailure("empty", "name", role.name());
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
