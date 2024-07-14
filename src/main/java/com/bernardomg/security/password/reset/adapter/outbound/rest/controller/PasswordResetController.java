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

package com.bernardomg.security.password.reset.adapter.outbound.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.Unsecured;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Handles password reset for a user which can't start a session. It is divided into two steps: starting the password
 * reset and the password change. All the logic is delegated to a {@link PasswordResetService}.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/password/reset")
@AllArgsConstructor
public class PasswordResetController {

    /**
     * Password reset service.
     */
    private final PasswordResetService service;

    /**
     * Change password at the end of a password recovery.
     *
     * @param token
     *            token for password change
     * @param request
     *            password change request
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Unsecured
    public void changePassword(@PathVariable("token") final String token,
            @Valid @RequestBody final PasswordResetChange request) {
        // TODO: return if it was successful
        service.changePassword(token, request.getPassword());
    }

    /**
     * Start a password reset.
     *
     * @param request
     *            password recovery request
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Unsecured
    public void startPasswordReset(@Valid @RequestBody final PasswordReset request) {
        // TODO: Hide exceptions for invalid user
        // TODO: return if it was successful
        service.startPasswordReset(request.getEmail());
    }

    /**
     * Validates a token.
     *
     * @param token
     *            token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    @GetMapping(path = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Unsecured
    public UserTokenStatus validateToken(@PathVariable("token") final String token) {
        // TODO: Use a generic token controller
        // TODO: Use cache
        return service.validateToken(token);
    }

}
