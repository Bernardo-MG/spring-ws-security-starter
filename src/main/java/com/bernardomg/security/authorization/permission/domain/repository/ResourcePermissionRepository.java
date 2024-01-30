
package com.bernardomg.security.authorization.permission.domain.repository;

import java.util.Collection;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;

public interface ResourcePermissionRepository {

    public ResourcePermission addPermission(final RolePermission permission);

    public boolean exists(final String name);

    public Collection<ResourcePermission> findAll();

    public Collection<ResourcePermission> findAllForUser(final String username);

    public Iterable<ResourcePermission> findAvailablePermissions(final String role, final Pageable pageable);

    public Iterable<ResourcePermission> findPermissionsForRole(final String role, final Pageable page);

    public ResourcePermission removePermission(final RolePermission permission);

    public ResourcePermission save(final ResourcePermission permission);

}
