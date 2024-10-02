
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.data.domain.model.Action;

public final class Actions {

    public static final Action create() {
        return new Action(PermissionConstants.CREATE);
    }

    private Actions() {
        super();
    }

}
