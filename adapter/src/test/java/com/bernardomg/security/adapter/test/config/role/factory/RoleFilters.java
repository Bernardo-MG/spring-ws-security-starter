
package com.bernardomg.security.adapter.test.config.role.factory;

import java.util.Optional;

import com.bernardomg.security.domain.role.filter.RoleFilter;

public final class RoleFilters {

    public static final RoleFilter byName() {
        return new RoleFilter(Optional.of(RoleConstants.NAME));
    }

    public static final RoleFilter byNameNotExisting() {
        return new RoleFilter(Optional.of(RoleConstants.ALTERNATIVE_NAME));
    }

    public static final RoleFilter empty() {
        return new RoleFilter(Optional.empty());
    }

}
