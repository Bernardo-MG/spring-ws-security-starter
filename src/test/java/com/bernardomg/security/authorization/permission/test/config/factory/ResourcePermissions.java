
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;

public final class ResourcePermissions {

    public static final ResourcePermission create() {
        return ResourcePermission.builder()
            .withName("DATA:CREATE")
            .withResource("DATA")
            .withAction("CREATE")
            .build();
    }

    public static final ResourcePermission delete() {
        return ResourcePermission.builder()
            .withName("DATA:DELETE")
            .withResource("DATA")
            .withAction("DELETE")
            .build();
    }

    public static final ResourcePermission read() {
        return ResourcePermission.builder()
            .withName("DATA:READ")
            .withResource("DATA")
            .withAction("READ")
            .build();
    }

    public static final ResourcePermission update() {
        return ResourcePermission.builder()
            .withName("DATA:UPDATE")
            .withResource("DATA")
            .withAction("UPDATE")
            .build();
    }

    private ResourcePermissions() {
        super();
    }

}
