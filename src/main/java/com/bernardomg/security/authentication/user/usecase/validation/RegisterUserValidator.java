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

package com.bernardomg.security.authentication.user.usecase.validation;

import java.util.Collection;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.AbstractValidator;

import lombok.extern.slf4j.Slf4j;

/**
 * Register user validation.
 * <p>
 * It applies the following rules:
 * <ul>
 * <li>The username is not registered</li>
 * <li>The email is not registered</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class RegisterUserValidator extends AbstractValidator<User> {

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    public RegisterUserValidator(final UserRepository userRepo) {
        super();

        userRepository = userRepo;
    }

    @Override
    protected final void checkRules(final User user, final Collection<FieldFailure> failures) {
        FieldFailure failure;

        // The username is not registered
        if (userRepository.exists(user.getUsername())) {
            log.error("A user already exists with the username {}", user.getUsername());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            failure = FieldFailure.of("username", "existing", user.getUsername());
            failures.add(failure);
        }

        // TODO: Don't give hints about existing emails
        // The email is not registered
        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("A user already exists with the username {}", user.getUsername());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            failure = FieldFailure.of("email", "existing", user.getEmail());
            failures.add(failure);
        }
    }

}
