
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourceEntity;

public final class ResourceEntities {

    public static final ResourceEntity data() {
        final ResourceEntity entity;

        entity = new ResourceEntity();
        entity.setName(PermissionConstants.DATA);

        return entity;
    }

    private ResourceEntities() {
        super();
    }

}
