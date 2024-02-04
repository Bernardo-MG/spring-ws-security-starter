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

package com.bernardomg.security.authorization.token.usecase.validation;

import java.time.LocalDateTime;
import java.util.Collection;

import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.validation.AbstractValidator;
import com.bernardomg.validation.failure.FieldFailure;

import lombok.extern.slf4j.Slf4j;

/**
 * Validator for patching user tokens.
 * <p>
 * Applies these rules:
 * <ul>
 * <li>The token expiration date is not before the current date</li>
 * <li>The token can be revoked, but revocation can't be reverted</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class PatchUserTokenValidator extends AbstractValidator<UserTokenPartial> {

    public PatchUserTokenValidator() {
        super();
    }

    @Override
    protected final void checkRules(final UserTokenPartial token, final Collection<FieldFailure> failures) {
        final LocalDateTime today;
        FieldFailure        failure;

        // Verify the expiration date is not before now
        today = LocalDateTime.now()
            .minusMinutes(2);
        if ((token.getExpirationDate() != null) && (token.getExpirationDate()
            .isBefore(today))) {
            log.error("Token expiration date {} is before now {}", token.getExpirationDate(), today);
            failure = FieldFailure.of("expirationDate", "beforeToday", token.getExpirationDate());
            failures.add(failure);
        }

        // Verify the token revoked flag is not cancelled
        if ((token.getRevoked() != null) && (!token.getRevoked())) {
            log.error("Reverting token revocation");
            failure = FieldFailure.of("revoked", "invalidValue", token.getRevoked());
            failures.add(failure);
        }
    }

}
