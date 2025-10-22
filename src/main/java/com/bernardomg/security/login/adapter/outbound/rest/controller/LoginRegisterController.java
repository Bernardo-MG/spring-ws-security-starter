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

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.web.WebSorting;
import com.bernardomg.security.access.annotation.RequireResourceAccess;
import com.bernardomg.security.login.adapter.outbound.cache.Logins;
import com.bernardomg.security.login.adapter.outbound.rest.model.LoginRegisterDtoMapper;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.usecase.service.LoginRegisterService;
import com.bernardomg.security.openapi.api.LoginRegisterApi;
import com.bernardomg.security.openapi.model.LoginRegisterPageResponseDto;
import com.bernardomg.security.permission.domain.constant.Actions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

/**
 * Login register controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class LoginRegisterController implements LoginRegisterApi {

    /**
     * Login register service.
     */
    private final LoginRegisterService service;

    public LoginRegisterController(final LoginRegisterService service) {
        super();

        this.service = service;
    }

    @Override
    @RequireResourceAccess(resource = "LOGIN_REGISTER", action = Actions.READ)
    @Cacheable(cacheNames = Logins.LOGIN_REGISTERS)
    public LoginRegisterPageResponseDto getAllLoginRegisters(@Min(1) @Valid final Integer page,
            @Min(1) @Valid final Integer size, @Valid final List<String> sort) {
        final Pagination    pagination;
        final Sorting       sorting;
        Page<LoginRegister> loginRegisters;

        pagination = new Pagination(page, size);
        sorting = WebSorting.toSorting(sort);
        loginRegisters = service.getAll(pagination, sorting);
        return LoginRegisterDtoMapper.toResponseDto(loginRegisters);
    }

}
