
package com.bernardomg.security.authorization.permission.domain.repository;

public interface RolePermissionRepository {

    public boolean exists(final String role, final String permission);

}
