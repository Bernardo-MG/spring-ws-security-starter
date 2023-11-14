
package com.bernardomg.security.authorization.permission.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;

public interface PermissionService {

    public Iterable<ResourcePermission> getAll(final Pageable pageable);

    public Optional<ResourcePermission> getOne(final long id);

}
