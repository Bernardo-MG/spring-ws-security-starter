
package com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository;

import java.util.Optional;

import org.springframework.data.domain.Example;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;

public final class JpaUserRoleRepository implements UserRoleRepository {

    private final RoleSpringRepository     roleRepository;

    private final UserSpringRepository     userRepository;

    private final UserRoleSpringRepository userRoleRepository;

    public JpaUserRoleRepository(final UserSpringRepository userRepo, final RoleSpringRepository roleRepo,
            final UserRoleSpringRepository userRoleRepo) {
        super();

        userRepository = userRepo;
        roleRepository = roleRepo;
        userRoleRepository = userRoleRepo;
    }

    @Override
    public final void delete(final String username, final String role) {
        final UserRoleEntity       userRole;
        final Optional<RoleEntity> readRole;
        final Optional<UserEntity> readUser;

        readUser = userRepository.findOneByUsername(username);
        readRole = roleRepository.findOneByName(role);

        userRole = UserRoleEntity.builder()
            .withUserId(readUser.get()
                .getId())
            .withRoleId(readRole.get()
                .getId())
            .build();

        userRoleRepository.delete(userRole);
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

    @Override
    public final void save(final String username, final String role) {
        final UserRoleEntity       userRole;
        final Optional<RoleEntity> readRole;
        final Optional<UserEntity> readUser;

        readUser = userRepository.findOneByUsername(username);
        readRole = roleRepository.findOneByName(role);

        userRole = UserRoleEntity.builder()
            .withUserId(readUser.get()
                .getId())
            .withRoleId(readRole.get()
                .getId())
            .build();

        userRoleRepository.save(userRole);
    }

}
