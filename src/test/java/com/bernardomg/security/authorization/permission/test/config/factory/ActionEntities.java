
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ActionEntity;

public final class ActionEntities {

    public static final ActionEntity create() {
        return ActionEntity.builder()
            .withName(PermissionConstants.CREATE)
            .build();
    }

    private ActionEntities() {
        super();
    }

}
