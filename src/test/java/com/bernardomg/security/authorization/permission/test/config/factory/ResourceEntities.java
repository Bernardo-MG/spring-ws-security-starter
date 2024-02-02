
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourceEntity;

public final class ResourceEntities {

    public static final ResourceEntity data() {
        return ResourceEntity.builder()
            .withName(PermissionConstants.DATA)
            .build();
    }

    private ResourceEntities() {
        super();
    }

}
