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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.login.adapter.outbound.cache.Logins;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.usecase.service.LoginRegisterService;
import com.bernardomg.security.permission.data.constant.Actions;

/**
 * Login register controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/login/register")
public class LoginRegisterController {

    /**
     * Login register service.
     */
    private final LoginRegisterService service;

    public LoginRegisterController(final LoginRegisterService service) {
        super();

        this.service = service;
    }

    /**
     * Returns all the log in registers in a paginated form.
     *
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return a page for the log in registers
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "LOGIN_REGISTER", action = Actions.READ)
    @Cacheable(cacheNames = Logins.LOGIN_REGISTERS)
    public Iterable<LoginRegister> readAll(final Pagination pagination, final Sorting sorting) {
        return service.getAll(pagination, sorting);
    }

}
