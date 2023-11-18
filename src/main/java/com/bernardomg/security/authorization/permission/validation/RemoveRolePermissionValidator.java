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

package com.bernardomg.security.authorization.permission.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

/**
 * Remove role permission validation.
 * <p>
 * It applies the following rules:
 * <ul>
 * <li>The role permission exists</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class RemoveRolePermissionValidator implements Validator<RolePermission> {

    private final RolePermissionRepository rolePermissionRepository;

    public RemoveRolePermissionValidator(final RolePermissionRepository rolePermissionRepo) {
        super();

        rolePermissionRepository = Objects.requireNonNull(rolePermissionRepo);
    }

    @Override
    public final void validate(final RolePermission relationship) {
        final Collection<FieldFailure> failures;
        FieldFailure                   failure;

        failures = new ArrayList<>();

        // The role permission exists
        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(relationship.getRoleId(),
            relationship.getPermissionId())) {
            log.error("Found no role permission for role {} and permission {}", relationship.getRoleId(),
                relationship.getPermissionId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            // TODO: Use the correct id
            failure = FieldFailure.of("rolePermission", "notExisting", relationship.getRoleId());
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
