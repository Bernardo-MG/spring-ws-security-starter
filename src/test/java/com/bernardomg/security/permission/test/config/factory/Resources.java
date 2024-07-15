
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.domain.model.Resource;

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
