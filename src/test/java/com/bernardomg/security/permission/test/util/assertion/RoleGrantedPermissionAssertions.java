
package com.bernardomg.security.permission.test.util.assertion;

import org.assertj.core.api.Assertions;

import com.bernardomg.security.authorization.permission.persistence.model.PersistentRoleGrantedPermission;

public final class RoleGrantedPermissionAssertions {

    public static final void isEqualTo(final PersistentRoleGrantedPermission received,
            final PersistentRoleGrantedPermission expected) {
        Assertions.assertThat(received.getAction())
            .withFailMessage("Expected action '%s' but got '%s'", expected.getAction(), received.getAction())
            .isEqualTo(expected.getAction());
        Assertions.assertThat(received.getResource())
            .withFailMessage("Expected resource '%s' but got '%s'", expected.getResource(), received.getResource())
            .isEqualTo(expected.getResource());
        Assertions.assertThat(received.getRoleId())
            .withFailMessage("Expected role id '%s' but got '%s'", expected.getRoleId(), received.getRoleId())
            .isEqualTo(expected.getRoleId());
        Assertions.assertThat(received.getRole())
            .withFailMessage("Expected role '%s' but got '%s'", expected.getRole(), received.getRole())
            .isEqualTo(expected.getRole());
    }

}
