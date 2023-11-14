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

package com.bernardomg.security.authorization.role.validation;

import java.util.ArrayList;
import java.util.Collection;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authorization.role.model.UserRole;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AddUserRoleValidator implements Validator<UserRole> {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public AddUserRoleValidator(final UserRepository userRepo, final RoleRepository roleRepo) {
        super();

        userRepository = userRepo;
        roleRepository = roleRepo;
    }

    @Override
    public final void validate(final UserRole relationship) {
        final Collection<FieldFailure> failures;
        FieldFailure                   failure;

        failures = new ArrayList<>();

        // The user exists
        if (!userRepository.existsById(relationship.getUserId())) {
            log.error("Found no user with id {}", relationship.getUserId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            failure = FieldFailure.of("id", "notExisting", relationship.getUserId());
            failures.add(failure);
        }

        // The action exists
        if (!roleRepository.existsById(relationship.getRoleId())) {
            log.error("Found no role with id {}", relationship.getRoleId());
            // TODO: Is the code not exists or is it not existing? Make sure all use the same
            failure = FieldFailure.of("role", "notExisting", relationship.getRoleId());
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
