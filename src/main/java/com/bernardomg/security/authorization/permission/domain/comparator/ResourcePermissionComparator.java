
package com.bernardomg.security.authorization.permission.domain.comparator;

import java.util.Comparator;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;

public final class ResourcePermissionComparator implements Comparator<ResourcePermission> {

    @Override
    public final int compare(final ResourcePermission left, final ResourcePermission right) {
        final String leftText;
        final String rightText;

        leftText = String.format("%s %s", left.getResource(), left.getAction());
        rightText = String.format("%s %s", right.getResource(), right.getAction());
        return leftText.compareTo(rightText);
    }

}
