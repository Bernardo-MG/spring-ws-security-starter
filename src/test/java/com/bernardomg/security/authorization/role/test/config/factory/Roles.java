
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.model.Role;

public final class Roles {

    public static final String ALTERNATIVE_NAME = "role2";

    public static final String NAME             = "role";

    public static final Role alternative() {
        return Role.builder()
            .withName(ALTERNATIVE_NAME)
            .build();
    }

    public static final Role valid() {
        return Role.builder()
            .withName(NAME)
            .build();
    }

    private Roles() {
        super();
    }

}
