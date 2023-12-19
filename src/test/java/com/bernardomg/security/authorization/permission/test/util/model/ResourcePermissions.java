
package com.bernardomg.security.authorization.permission.test.util.model;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;

public final class ResourcePermissions {

    public static final ResourcePermission create() {
        return ResourcePermission.builder()
            .withName("DATA:CREATE")
            .withId(1L)
            .withResource("DATA")
            .withAction("CREATE")
            .build();
    }

    public static final ResourcePermission delete() {
        return ResourcePermission.builder()
            .withName("DATA:DELETE")
            .withId(4L)
            .withResource("DATA")
            .withAction("DELETE")
            .build();
    }

    public static final ResourcePermission read() {
        return ResourcePermission.builder()
            .withName("DATA:READ")
            .withId(2L)
            .withResource("DATA")
            .withAction("READ")
            .build();
    }

    public static final ResourcePermission update() {
        return ResourcePermission.builder()
            .withName("DATA:UPDATE")
            .withId(3L)
            .withResource("DATA")
            .withAction("UPDATE")
            .build();
    }

    private ResourcePermissions() {
        super();
    }

}
