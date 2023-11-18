
package com.bernardomg.security.authorization.role.test.util.model;

import com.bernardomg.security.authorization.role.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.model.request.RoleQueryRequest;

public final class RolesQuery {

    public static final RoleQuery empty() {
        return RoleQueryRequest.builder()
            .build();
    }

}
