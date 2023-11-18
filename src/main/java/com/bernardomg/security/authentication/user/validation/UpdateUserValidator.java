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

package com.bernardomg.security.authentication.user.validation;

import java.util.Collection;

import com.bernardomg.security.authentication.user.model.query.UserUpdate;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.validation.AbstractValidator;
import com.bernardomg.validation.failure.FieldFailure;

import lombok.extern.slf4j.Slf4j;

/**
 * Update user validation.
 * <p>
 * It applies the following rules:
 * <ul>
 * <li>The email is not registered</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class UpdateUserValidator extends AbstractValidator<UserUpdate> {

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    public UpdateUserValidator(final UserRepository userRepo) {
        super();

        userRepository = userRepo;
    }

    @Override
    protected final void checkRules(final UserUpdate user, final Collection<FieldFailure> failures) {
        FieldFailure failure;

        // Verify the email is not registered
        if (userRepository.existsByIdNotAndEmail(user.getId(), user.getEmail())) {
            log.error("A user already exists with the email {}", user.getEmail());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            failure = FieldFailure.of("email", "existing", user.getEmail());
            failures.add(failure);
        }

    }

}
