
package com.bernardomg.security.authorization.role.domain.repository;

public interface UserRoleRepository {

    public void delete(final String username, final String role);

    public boolean existsForRole(final String role);

    public void save(final String username, final String role);

}
