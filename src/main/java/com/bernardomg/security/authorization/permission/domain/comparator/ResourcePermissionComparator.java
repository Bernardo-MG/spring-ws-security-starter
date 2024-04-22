
package com.bernardomg.security.authorization.permission.domain.comparator;

import java.util.Comparator;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;

public final class ResourcePermissionComparator implements Comparator<ResourcePermission> {

    @Override
    public final int compare(final ResourcePermission left, final ResourcePermission right) {
        return left.getName()
            .compareTo(right.getName());
    }

}
