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

import org.springframework.data.domain.Example;

import com.bernardomg.security.authorization.role.model.request.RoleCreate;
import com.bernardomg.security.authorization.role.persistence.model.RoleEntity;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.validation.Validator;
import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

/**
 * Create role validation.
 * <p>
 * It applies the following rules:
 * <ul>
 * <li>The role name doesn't exist</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class CreateRoleValidator implements Validator<RoleCreate> {

    private final RoleRepository roleRepository;

    public CreateRoleValidator(final RoleRepository roleRepo) {
        super();

        roleRepository = roleRepo;
    }

    @Override
    public final void validate(final RoleCreate role) {
        final Collection<FieldFailure> failures;
        final FieldFailure             failure;
        final RoleEntity               sample;

        failures = new ArrayList<>();

        sample = RoleEntity.builder()
            .withName(role.getName())
            .build();

        // The role name doesn't exist
        if (roleRepository.exists(Example.of(sample))) {
            log.error("A role already exists with the name {}", role.getName());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            failure = FieldFailure.of("name", "existing", role.getName());
            failures.add(failure);
        }

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

}
