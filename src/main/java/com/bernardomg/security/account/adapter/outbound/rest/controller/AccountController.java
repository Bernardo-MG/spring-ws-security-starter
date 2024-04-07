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

package com.bernardomg.security.account.adapter.outbound.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.Unsecured;
import com.bernardomg.security.account.adapter.outbound.rest.model.AccountChange;
import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.account.usecase.service.AccountService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Handles account requests. All the logic is delegated to a {@link AccountService}.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    /**
     * Account service.
     */
    private final AccountService service;

    /**
     * Attempts to acquire the current user account. If the user is not authenticated, then it returns null.
     *
     * @return the current user account
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Unsecured
    public Account currentUser() {
        return service.getCurrentUser()
            .orElse(null);
    }

    /**
     * Updates an account.
     *
     * @param request
     *            updated account data
     * @return the updated account
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Unsecured
    public Account updateForCurrentUser(@Valid @RequestBody final AccountChange request) {
        final Account account;

        account = BasicAccount.builder()
            .withName(request.getName())
            .build();

        return service.update(account);
    }

}
