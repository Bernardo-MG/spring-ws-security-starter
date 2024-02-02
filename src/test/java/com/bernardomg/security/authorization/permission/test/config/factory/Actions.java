
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.domain.model.Action;

public final class Actions {

    public static final Action create() {
        return Action.builder()
            .withName(PermissionConstants.CREATE)
            .build();
    }

    private Actions() {
        super();
    }

}
