
package com.bernardomg.security.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Example;

import com.bernardomg.security.user.persistence.model.PersistentRole;
import com.bernardomg.security.user.persistence.model.PersistentUser;
import com.bernardomg.security.user.persistence.model.PersistentUserRole;
import com.bernardomg.security.user.persistence.repository.RoleRepository;
import com.bernardomg.security.user.persistence.repository.UserRepository;
import com.bernardomg.security.user.persistence.repository.UserRoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TestUsersInitializer implements ApplicationRunner {

    private final RoleRepository     roleRepository;

    private final UserRepository     userRepository;

    private final UserRoleRepository userRoleRepository;

    public TestUsersInitializer(final UserRepository userRepo, final UserRoleRepository userRoleRepo,
            final RoleRepository roleRepo) {
        super();

        userRepository = userRepo;
        userRoleRepository = userRoleRepo;
        roleRepository = roleRepo;
    }

    @Override
    public final void run(final ApplicationArguments args) throws Exception {
        log.debug("Initializing test users");

        runIfNotExists(this::initializeRootUser, "root");
        runIfNotExists(this::initializeReadUser, "read");

        log.debug("Initialized test users");
    }

    private final PersistentUser getReadUser() {
        return PersistentUser.builder()
            .username("read")
            .name("read")
            .email("email2@nowhere.com")
            .password("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW")
            .enabled(true)
            .locked(false)
            .expired(false)
            .passwordExpired(false)
            .build();
    }

    private final PersistentUser getRootUser() {
        return PersistentUser.builder()
            .username("root")
            .name("root")
            .email("email1@nowhere.com")
            .password("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW")
            .enabled(true)
            .locked(false)
            .expired(false)
            .passwordExpired(false)
            .build();
    }

    private final void initializeReadUser() {
        final PersistentUser     readUser;
        final PersistentUser     savedReadUser;
        final PersistentUserRole readUserRole;
        final PersistentRole     example;
        final PersistentRole     role;

        // Add read user
        readUser = getReadUser();
        savedReadUser = userRepository.save(readUser);

        example = PersistentRole.builder()
            .name("READ")
            .build();
        role = roleRepository.findOne(Example.of(example))
            .get();

        readUserRole = PersistentUserRole.builder()
            .userId(savedReadUser.getId())
            .roleId(role.getId())
            .build();
        userRoleRepository.save(readUserRole);
    }

    private final void initializeRootUser() {
        final PersistentUser     rootUser;
        final PersistentUser     savedRootUser;
        final PersistentUserRole rootUserRole;
        final PersistentRole     example;
        final PersistentRole     role;

        // Add root user
        rootUser = getRootUser();
        savedRootUser = userRepository.save(rootUser);

        example = PersistentRole.builder()
            .name("ADMIN")
            .build();
        role = roleRepository.findOne(Example.of(example))
            .get();

        rootUserRole = PersistentUserRole.builder()
            .userId(savedRootUser.getId())
            .roleId(role.getId())
            .build();
        userRoleRepository.save(rootUserRole);
    }

    private final void runIfNotExists(final Runnable runnable, final String name) {
        if (!userRepository.existsByUsername(name)) {
            runnable.run();
            log.debug("Initialized {} user", name);
        } else {
            log.debug("User {} already exists. Skipped initialization.", name);
        }
    }

}
