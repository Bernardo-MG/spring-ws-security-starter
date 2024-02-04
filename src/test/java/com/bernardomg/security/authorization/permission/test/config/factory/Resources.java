
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.domain.model.Resource;

public final class Resources {

    public static final Resource data() {
        return Resource.builder()
            .withName(PermissionConstants.DATA)
            .build();
    }

    private Resources() {
        super();
    }

}
