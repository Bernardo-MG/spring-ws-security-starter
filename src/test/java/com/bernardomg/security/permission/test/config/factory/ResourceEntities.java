
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourceEntity;

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
