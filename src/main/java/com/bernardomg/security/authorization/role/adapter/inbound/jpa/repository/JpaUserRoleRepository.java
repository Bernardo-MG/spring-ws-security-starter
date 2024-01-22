
package com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository;

import org.springframework.data.domain.Example;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;

public final class JpaUserRoleRepository implements UserRoleRepository {

    private final RoleSpringRepository     roleRepository;

    private final UserRoleSpringRepository userRoleRepository;

    public JpaUserRoleRepository(final RoleSpringRepository roleRepo, final UserRoleSpringRepository userRoleRepo) {
        super();

        roleRepository = roleRepo;
        userRoleRepository = userRoleRepo;
    }

    @Override
    public final boolean existsForRole(final String role) {
        final UserRoleEntity sample;
        final RoleEntity     roleEntity;

        roleEntity = roleRepository.findOneByName(role)
            .get();
        sample = UserRoleEntity.builder()
            .withRoleId(roleEntity.getId())
            .build();

        return userRoleRepository.exists(Example.of(sample));
    }

}
