
package com.bernardomg.security.permission.test.util.assertion;

import org.assertj.core.api.Assertions;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;

public final class RolePermissionAssertions {

    public static final void isEqualTo(final ResourcePermission received, final ResourcePermission expected) {
        Assertions.assertThat(received.getAction())
            .withFailMessage("Expected action '%s' but got '%s'", expected.getAction(), received.getAction())
            .isEqualTo(expected.getAction());
        Assertions.assertThat(received.getResource())
            .withFailMessage("Expected resource '%s' but got '%s'", expected.getResource(), received.getResource())
            .isEqualTo(expected.getResource());
    }

    public static final void isEqualTo(final RolePermissionEntity received, final RolePermissionEntity expected) {
        Assertions.assertThat(received.getPermission())
            .withFailMessage("Expected permission '%s' but got '%s'", expected.getPermission(),
                received.getPermission())
            .isEqualTo(expected.getPermission());
        Assertions.assertThat(received.getRoleId())
            .withFailMessage("Expected role id '%s' but got '%s'", expected.getRoleId(), received.getRoleId())
            .isEqualTo(expected.getRoleId());
        Assertions.assertThat(received.getGranted())
            .withFailMessage("Expected granted flag '%s' but got '%s'", expected.getGranted(), received.getGranted())
            .isEqualTo(expected.getGranted());
    }

}
