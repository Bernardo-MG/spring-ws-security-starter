
package com.bernardomg.security.authorization.permission.domain.repository;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;

public interface ResourcePermissionRepository {

    public ResourcePermission addPermission(final String role, final String permission);

    public boolean exists(final String name);

    public Iterable<ResourcePermission> findAvailablePermissions(final String role, final Pageable pageable);

    public Iterable<ResourcePermission> findPermissionsForRole(final String role, final Pageable page);

    public ResourcePermission removePermission(final String role, final String permission);

    public ResourcePermission save(final ResourcePermission permission);

}
