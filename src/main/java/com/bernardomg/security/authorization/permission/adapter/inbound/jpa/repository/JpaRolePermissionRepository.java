/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Optional;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;

/**
 * Role permission repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class JpaRolePermissionRepository implements RolePermissionRepository {

    private final RolePermissionSpringRepository rolePermissionRepository;

    private final RoleSpringRepository           roleRepository;

    public JpaRolePermissionRepository(final RoleSpringRepository roleRepo,
            final RolePermissionSpringRepository rolePermissionRepo) {
        super();

        roleRepository = roleRepo;
        rolePermissionRepository = rolePermissionRepo;
    }

    @Override
    public final boolean exists(final String role, final String permission) {
        final Optional<RoleEntity> readRole;
        final long                 id;

        readRole = roleRepository.findOneByName(role);
        id = readRole.get()
            .getId();
        return rolePermissionRepository.existsByRoleIdAndPermissionAndGranted(id, permission, true);
    }

    @Override
    public final RolePermission save(final RolePermission permission) {
        final RolePermissionEntity rolePermission;
        final Optional<RoleEntity> role;
        final RolePermissionEntity saved;
        final RolePermission       created;

        role = roleRepository.findOneByName(permission.getRole());
        if (role.isPresent()) {
            rolePermission = RolePermissionEntity.builder()
                .withRoleId(role.get()
                    .getId())
                .withPermission(permission.getPermission())
                .withGranted(true)
                .build();
            saved = rolePermissionRepository.save(rolePermission);

            created = toDomain(saved, role.get());
        } else {
            created = RolePermission.builder()
                .build();
        }

        return created;
    }

    private final RolePermission toDomain(final RolePermissionEntity permission, final RoleEntity role) {
        return RolePermission.builder()
            .withPermission(permission.getPermission())
            .withRole(role.getName())
            .build();
    }

}
