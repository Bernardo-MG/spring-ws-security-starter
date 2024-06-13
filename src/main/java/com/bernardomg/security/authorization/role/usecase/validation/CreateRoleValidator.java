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

import java.util.Collection;

import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.AbstractValidator;

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
public final class CreateRoleValidator extends AbstractValidator<Role> {

    /**
     * Role repository.
     */
    private final RoleRepository roleRepository;

    public CreateRoleValidator(final RoleRepository roleRepo) {
        super();

        roleRepository = roleRepo;
    }

    @Override
    protected final void checkRules(final Role role, final Collection<FieldFailure> failures) {
        final FieldFailure failure;

        // The role name doesn't exist
        if (roleRepository.exists(role.getName())) {
            log.error("A role already exists with the name {}", role.getName());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            failure = FieldFailure.of("name", "existing", role.getName());
            failures.add(failure);
        }
    }

}
