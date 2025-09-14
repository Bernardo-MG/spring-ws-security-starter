/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.password.reset.adapter.outbound.rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.Unsecured;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.ucronia.openapi.api.PasswordResetRequestApi;
import com.bernardomg.ucronia.openapi.model.RequestPasswordResetDto;

import jakarta.validation.Valid;

/**
 * Handles password reset for a user, usually when it can't start a session. It is divided into two steps:
 * <p>
 * <ul>
 * <li>Starting the password reset</li>
 * <li>Changing password at end of password reset</li>
 * </ul>
 * <p>
 * All the logic is delegated to a {@link PasswordResetService}.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class PasswordResetRequestController implements PasswordResetRequestApi {

    /**
     * Password reset service.
     */
    private final PasswordResetService service;

    public PasswordResetRequestController(final PasswordResetService service) {
        super();

        this.service = service;
    }

    @Override
    @Unsecured
    public void requestPasswordReset(@Valid final RequestPasswordResetDto requestPasswordResetDto) {
        service.startPasswordReset(requestPasswordResetDto.getEmail());
    }

}
