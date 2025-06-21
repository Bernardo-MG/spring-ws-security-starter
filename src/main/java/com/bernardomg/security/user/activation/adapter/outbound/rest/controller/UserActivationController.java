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

package com.bernardomg.security.user.activation.adapter.outbound.rest.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
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
import com.bernardomg.security.user.activation.adapter.outbound.rest.model.UserActivation;
import com.bernardomg.security.user.activation.usecase.service.UserActivationService;
import com.bernardomg.security.user.data.adapter.outbound.cache.UserCaches;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;

import jakarta.validation.Valid;

/**
 * User activation REST controller. This requires a token, given to the new user when he registers.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/user/activate")
public class UserActivationController {

    /**
     * Service which handles user activation.
     */
    private final UserActivationService service;

    public UserActivationController(final UserActivationService service) {
        super();

        this.service = service;
    }

    /**
     * Activates a new user.
     *
     * @param token
     *            token identifying the user to activate.
     * @param request
     *            additional data required for activation
     * @return the newly activated user
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(path = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Unsecured
    @Caching(put = { @CachePut(cacheNames = UserCaches.USER, key = "#result.username") },
            evict = { @CacheEvict(cacheNames = UserCaches.USERS, allEntries = true) })
    public User activate(@PathVariable("token") final String token, @Valid @RequestBody final UserActivation request) {
        // TODO: return only the necessary data
        return service.activateUser(token, request.password());
    }

    /**
     * Verifies the token is valid.
     *
     * @param token
     *            token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    @GetMapping(path = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Unsecured
    public UserTokenStatus validateToken(@PathVariable("token") final String token) {
        // TODO: Use a generic controller for tokens
        // TODO: Use cache
        return service.validateToken(token);
    }

}
