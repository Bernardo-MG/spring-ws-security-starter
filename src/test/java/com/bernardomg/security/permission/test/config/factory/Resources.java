
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.data.domain.model.Resource;

public final class Resources {

    public static final Resource data() {
        return new Resource(PermissionConstants.DATA);
    }

    private Resources() {
        super();
    }

}
