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

package com.bernardomg.security.login.adapter.outbound.rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.annotation.Unsecured;
import com.bernardomg.security.login.adapter.outbound.rest.model.LoginDtoMapper;
import com.bernardomg.security.login.domain.model.Credentials;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.login.usecase.service.LoginService;
import com.bernardomg.security.openapi.api.LoginApi;
import com.bernardomg.security.openapi.model.LoginRequestDto;
import com.bernardomg.security.openapi.model.TokenLoginStatusResponseDto;

import jakarta.validation.Valid;

/**
 * Handles login requests. All the logic is delegated to a {@link LoginService}.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class LoginController implements LoginApi {

    /**
     * Login service.
     */
    private final LoginService service;

    public LoginController(final LoginService service) {
        super();

        this.service = service;
    }

    @Override
    @Unsecured
    public TokenLoginStatusResponseDto login(@Valid final LoginRequestDto loginRequestDto) {
        final Credentials      credentials;
        final TokenLoginStatus status;

        credentials = LoginDtoMapper.toDomain(loginRequestDto);
        status = service.login(credentials);

        return LoginDtoMapper.toResponseDto(status);
    }

}
