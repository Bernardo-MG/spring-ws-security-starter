
package com.bernardomg.security.permission.test.util.assertion;

import org.assertj.core.api.Assertions;

import com.bernardomg.security.authorization.permission.persistence.model.RoleGrantedPermissionEntity;

public final class RoleGrantedPermissionAssertions {

    public static final void isEqualTo(final RoleGrantedPermissionEntity received,
            final RoleGrantedPermissionEntity expected) {
        // TODO: This is not being used
        Assertions.assertThat(received.getAction())
            .withFailMessage("Expected action '%s' but got '%s'", expected.getAction(), received.getAction())
            .isEqualTo(expected.getAction());
        Assertions.assertThat(received.getRoleId())
            .withFailMessage("Expected role id '%s' but got '%s'", expected.getRoleId(), received.getRoleId())
            .isEqualTo(expected.getRoleId());
        Assertions.assertThat(received.getGranted())
            .withFailMessage("Expected granted '%s' but got '%s'", expected.getGranted(), received.getGranted())
            .isEqualTo(expected.getGranted());
    }

}
