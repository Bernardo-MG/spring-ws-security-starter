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

package com.bernardomg.security.user.token.adapter.outbound.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.permission.data.constant.Actions;
import com.bernardomg.security.user.token.adapter.outbound.rest.model.UserTokenPartial;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.usecase.service.UserTokenService;

/**
 * User token REST controller. Supports reading and patching, as the token are generated there is little which the user
 * can modify.
 * <p>
 * TODO: Try to add caching. Can't be done in the controller as the tokens are generated from services
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/user/token")
public class UserTokenController {

    /**
     * User token service.
     */
    private final UserTokenService service;

    public UserTokenController(final UserTokenService service) {
        super();

        this.service = service;
    }

    /**
     * Applies a partial change into a user token.
     *
     * @param tokenCode
     *            token for the user token to patch
     * @param request
     *            partial change to apply
     * @return the updated user token
     */
    @PatchMapping(path = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER_TOKEN", action = Actions.UPDATE)
    public UserToken patch(@PathVariable("token") final String tokenCode, @RequestBody final UserTokenPartial request) {
        final UserToken token;

        token = new UserToken("", "", "", tokenCode, null, request.expirationDate(), null, request.revoked());
        return service.patch(token);
    }

    /**
     * Reads all the user tokens paged.
     *
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return all the user tokens paged
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER_TOKEN", action = Actions.READ)
    public Iterable<UserToken> readAll(final Pagination pagination, final Sorting sorting) {
        return service.getAll(pagination, sorting);
    }

    /**
     * Reads a single user token. Otherwise {@code null} is returned.
     *
     * @param token
     *            token for the user token to read
     * @return the user token for the id, if it exists, or {@code null} otherwise
     */
    @GetMapping(path = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER_TOKEN", action = Actions.READ)
    public UserToken readOne(@PathVariable("token") final String token) {
        return service.getOne(token)
            .orElse(null);
    }

}
