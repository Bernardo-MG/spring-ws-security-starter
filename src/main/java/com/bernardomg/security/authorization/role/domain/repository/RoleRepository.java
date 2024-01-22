
package com.bernardomg.security.authorization.role.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;

public interface RoleRepository {

    public void delete(final String name);

    public boolean exists(final String name);

    public Iterable<Role> findAll(final RoleQuery query, final Pageable page);

    public Optional<Role> findOne(final String name);

    public Role save(final Role role);

}
