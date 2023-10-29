
package com.bernardomg.security.authorization.permission.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.Permission;

public interface PermissionService {

    public Iterable<Permission> getAll(final Pageable pageable);

    public Optional<Permission> getOne(final long id);

}
