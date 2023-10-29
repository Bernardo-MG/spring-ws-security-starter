
package com.bernardomg.security.permission.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.permission.model.Permission;

public interface PermissionService {

    public Iterable<Permission> getAll(final Pageable pageable);

    public Optional<Permission> getOne(final long id);

}
