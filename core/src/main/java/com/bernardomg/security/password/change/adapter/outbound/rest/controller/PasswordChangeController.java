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

package com.bernardomg.security.password.change.adapter.outbound.rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.annotation.Unsecured;
import com.bernardomg.security.openapi.api.PasswordChangeApi;
import com.bernardomg.security.openapi.model.PasswordChangeDto;
import com.bernardomg.security.password.change.usecase.service.PasswordChangeService;

import jakarta.validation.Valid;

/**
 * Handles changing the password for a user in session. All the logic is delegated to a {@link PasswordChangeService}.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class PasswordChangeController implements PasswordChangeApi {

    /**
     * Password recovery service.
     */
    private final PasswordChangeService service;

    public PasswordChangeController(final PasswordChangeService service) {
        super();

        this.service = service;
    }

    @Override
    @Unsecured
    public void changePassword(@Valid final PasswordChangeDto passwordChangeDto) {
        // TODO: return if it was successful
        service.changePasswordForUserInSession(passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());

    }

}
