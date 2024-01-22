
package com.bernardomg.security.authorization.role.domain.repository;

public interface UserRoleRepository {

    public boolean existsForRole(final String role);

}
