
package com.bernardomg.security.authorization.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.model.DtoRole;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.request.RoleCreate;
import com.bernardomg.security.authorization.role.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.model.request.RoleUpdate;
import com.bernardomg.security.authorization.role.persistence.model.PersistentRole;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.validation.CreateRoleValidator;
import com.bernardomg.security.authorization.role.validation.DeleteRoleValidator;
import com.bernardomg.security.authorization.role.validation.UpdateRoleValidator;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultRoleService implements RoleService {

    private final RoleRepository        roleRepository;

    private final Validator<RoleCreate> validatorCreateRole;

    private final Validator<Long>       validatorDeleteRole;

    private final Validator<RoleUpdate> validatorUpdateRole;

    public DefaultRoleService(final RoleRepository roleRepo, final UserRoleRepository userRoleRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo);

        validatorCreateRole = new CreateRoleValidator(roleRepo);
        validatorUpdateRole = new UpdateRoleValidator();
        validatorDeleteRole = new DeleteRoleValidator(roleRepo, userRoleRepo);
    }

    @Override
    public final Role create(final RoleCreate role) {
        final PersistentRole entity;
        final PersistentRole created;

        log.debug("Creating role {}", role);

        validatorCreateRole.validate(role);

        entity = toEntity(role);

        created = roleRepository.save(entity);

        return toDto(created);
    }

    @Override
    public final Boolean delete(final long id) {

        log.debug("Deleting role {}", id);

        validatorDeleteRole.validate(id);

        roleRepository.deleteById(id);

        return true;
    }

    @Override
    public final Iterable<Role> getAll(final RoleQuery sample, final Pageable pageable) {
        final PersistentRole entitySample;

        log.debug("Reading roles with sample {} and pagination {}", sample, pageable);

        entitySample = toEntity(sample);

        return roleRepository.findAll(Example.of(entitySample), pageable)
            .map(this::toDto);
    }

    @Override
    public final Optional<Role> getOne(final long id) {

        log.debug("Reading role with id {}", id);

        // TODO: Use the read optional
        if (!roleRepository.existsById(id)) {
            throw new MissingRoleIdException(id);
        }

        return roleRepository.findById(id)
            .map(this::toDto);
    }

    @Override
    public final Role update(final long id, final RoleUpdate role) {
        final PersistentRole entity;
        final PersistentRole created;

        log.debug("Updating role with id {} using data {}", id, role);

        if (!roleRepository.existsById(id)) {
            throw new MissingRoleIdException(id);
        }

        validatorUpdateRole.validate(role);

        entity = toEntity(role);
        entity.setId(id);

        created = roleRepository.save(entity);

        return toDto(created);
    }

    private final DtoRole toDto(final PersistentRole role) {
        return DtoRole.builder()
            .id(role.getId())
            .name(role.getName())
            .build();
    }

    private final PersistentRole toEntity(final RoleCreate role) {
        return PersistentRole.builder()
            .name(role.getName())
            .build();
    }

    private final PersistentRole toEntity(final RoleQuery role) {
        return PersistentRole.builder()
            .name(role.getName())
            .build();
    }

    private final PersistentRole toEntity(final RoleUpdate role) {
        return PersistentRole.builder()
            .id(role.getId())
            .name(role.getName())
            .build();
    }

}
