
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.adapter.inbound.jpa.model.permission.ActionEntity;

public final class ActionEntities {

    public static final ActionEntity create() {
        final ActionEntity entity;

        entity = new ActionEntity();
        entity.setName(PermissionConstants.CREATE);

        return entity;
    }

    private ActionEntities() {
        super();
    }

}
