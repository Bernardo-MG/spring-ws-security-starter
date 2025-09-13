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

package com.bernardomg.security.account.adapter.outbound.rest.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.Unsecured;
import com.bernardomg.security.account.adapter.outbound.rest.model.AccountDtoMapper;
import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.account.usecase.service.AccountService;
import com.bernardomg.ucronia.openapi.api.AccountApi;
import com.bernardomg.ucronia.openapi.model.AccountChangeDto;
import com.bernardomg.ucronia.openapi.model.AccountResponseDto;

import jakarta.validation.Valid;

/**
 * Handles account requests. All the logic is delegated to a {@link AccountService}.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class AccountController implements AccountApi {

    /**
     * Account service.
     */
    private final AccountService service;

    public AccountController(final AccountService service) {
        super();

        this.service = service;
    }

    @Override
    @Unsecured
    public AccountResponseDto getCurrentUserAccount() {
        final Optional<Account> account;

        account = service.getCurrentUser();

        return AccountDtoMapper.toResponseDto(account);
    }

    @Override
    @Unsecured
    public AccountResponseDto updateCurrentUserAccount(@Valid final AccountChangeDto accountChangeDto) {
        final Account account;
        final Account updated;

        account = BasicAccount.of(accountChangeDto.getName());

        updated = service.update(account);

        return AccountDtoMapper.toResponseDto(updated);
    }

}
