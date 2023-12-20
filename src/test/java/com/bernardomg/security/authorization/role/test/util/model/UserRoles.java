
package com.bernardomg.security.authorization.role.test.util.model;

import com.bernardomg.security.authorization.role.model.UserRole;

public final class UserRoles {

    public static final String ALTERNATIVE_NAME = "role2";

    public static final String NAME             = "role";

    public static final UserRole valid() {
        return UserRole.builder()
            .withRoleId(1L)
            .withUserId(1L)
            .build();
    }

    private UserRoles() {
        super();
    }

}
