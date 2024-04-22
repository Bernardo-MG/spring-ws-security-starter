
package com.bernardomg.security.authorization.role.domain.comparator;

import java.util.Comparator;

import com.bernardomg.security.authorization.role.domain.model.Role;

public final class RoleComparator implements Comparator<Role> {

    @Override
    public final int compare(final Role left, final Role right) {
        return left.getName()
            .compareTo(right.getName());
    }

}
