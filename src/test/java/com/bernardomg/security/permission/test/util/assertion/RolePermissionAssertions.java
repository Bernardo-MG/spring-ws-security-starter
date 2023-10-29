
package com.bernardomg.security.permission.test.util.assertion;

import org.assertj.core.api.Assertions;

import com.bernardomg.security.permission.model.Permission;
import com.bernardomg.security.permission.persistence.model.PersistentRolePermission;

public final class RolePermissionAssertions {

    public static final void isEqualTo(final Permission received, final Permission expected) {
        Assertions.assertThat(received.getAction())
            .withFailMessage("Expected action '%s' but got '%s'", expected.getAction(), received.getAction())
            .isEqualTo(expected.getAction());
        Assertions.assertThat(received.getResource())
            .withFailMessage("Expected resource '%s' but got '%s'", expected.getResource(), received.getResource())
            .isEqualTo(expected.getResource());
    }

    public static final void isEqualTo(final PersistentRolePermission received,
            final PersistentRolePermission expected) {
        Assertions.assertThat(received.getPermissionId())
            .withFailMessage("Expected permission id '%s' but got '%s'", expected.getPermissionId(),
                received.getPermissionId())
            .isEqualTo(expected.getPermissionId());
        Assertions.assertThat(received.getRoleId())
            .withFailMessage("Expected role id '%s' but got '%s'", expected.getRoleId(), received.getRoleId())
            .isEqualTo(expected.getRoleId());
        Assertions.assertThat(received.getGranted())
            .withFailMessage("Expected granted flag '%s' but got '%s'", expected.getGranted(), received.getGranted())
            .isEqualTo(expected.getGranted());
    }

}
