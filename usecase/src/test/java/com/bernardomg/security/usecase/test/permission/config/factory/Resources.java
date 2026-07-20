
package com.bernardomg.security.usecase.test.permission.config.factory;

import com.bernardomg.security.domain.permission.model.Resource;

public final class Resources {

    public static final Resource data() {
        return new Resource(PermissionConstants.DATA);
    }

    private Resources() {
        super();
    }

}
