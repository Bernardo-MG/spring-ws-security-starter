
package com.bernardomg.security.adapter.test.config.permission.factory;

import com.bernardomg.security.adapter.inbound.jpa.model.permission.ResourceEntity;

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
