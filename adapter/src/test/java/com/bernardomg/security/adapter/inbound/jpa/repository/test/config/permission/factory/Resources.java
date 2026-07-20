
package com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.factory;

import com.bernardomg.security.domain.permission.model.Resource;

public final class Resources {

    public static final Resource data() {
        return new Resource(PermissionConstants.DATA);
    }

    private Resources() {
        super();
    }

}
