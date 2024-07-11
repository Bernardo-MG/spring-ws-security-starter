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

package com.bernardomg.security.authentication.password.reset.usecase.service;

import com.bernardomg.security.token.domain.model.UserTokenStatus;

/**
 * Password reset service. Handles the steps requires to change a password for a user which can't start a session.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface PasswordResetService {

    /**
     * Change password.
     *
     * @param token
     *            token for the password change
     * @param newPassword
     *            new password
     */
    public void changePassword(final String token, final String newPassword);

    /**
     * Starts the password reset for a user, identified by the mail.
     *
     * @param email
     *            email for resetting the user password
     */
    public void startPasswordReset(final String email);

    /**
     * Validate a password recovery token.
     *
     * @param token
     *            token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public UserTokenStatus validateToken(final String token);

}
