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

package com.bernardomg.security.authorization.role.usecase.validation;

import java.util.ArrayList;
import java.util.Collection;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

/**
 * Update role validation.
 * <p>
 * It applies the following rules:
 * <ul>
 * <li>Can't have duplicated permissions</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class UpdateRoleValidator implements Validator<Role> {

    public UpdateRoleValidator() {
        super();
    }

    @Override
    public final void validate(final Role role) {
        final Collection<FieldFailure> failures;
        final long                     uniquePermissions;
        final int                      totalPermissions;
        final long                     duplicates;
        FieldFailure                   failure;

        failures = new ArrayList<>();

        // Verify there are no duplicated roles
        uniquePermissions = role.getPermissions()
            .stream()
            .map(ResourcePermission::getName)
            .distinct()
            .count();
        totalPermissions = role.getPermissions()
            .size();
        if (uniquePermissions < totalPermissions) {
            duplicates = (totalPermissions - uniquePermissions);
            log.error("Received {} permissions, but {} are duplicates", totalPermissions, duplicates);
            failure = FieldFailure.of("roles[]", "duplicated", duplicates);
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
