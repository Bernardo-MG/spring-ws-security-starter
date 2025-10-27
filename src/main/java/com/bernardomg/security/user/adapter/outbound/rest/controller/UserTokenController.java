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

package com.bernardomg.security.user.adapter.outbound.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.web.WebSorting;
import com.bernardomg.security.access.annotation.RequireResourceAuthorization;
import com.bernardomg.security.openapi.api.UserTokensApi;
import com.bernardomg.security.openapi.model.UserTokenChangeDto;
import com.bernardomg.security.openapi.model.UserTokenPageResponseDto;
import com.bernardomg.security.openapi.model.UserTokenResponseDto;
import com.bernardomg.security.permission.domain.constant.Actions;
import com.bernardomg.security.user.adapter.outbound.rest.model.UserTokenDtoMapper;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.usecase.service.UserTokenService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

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
public class UserTokenController implements UserTokensApi {

    /**
     * User token service.
     */
    private final UserTokenService service;

    public UserTokenController(final UserTokenService service) {
        super();

        this.service = service;
    }

    @Override
    @RequireResourceAuthorization(resource = "USER_TOKEN", action = Actions.READ)
    public UserTokenPageResponseDto getAllUserTokens(@Min(1) @Valid final Integer page,
            @Min(1) @Valid final Integer size, @Valid final List<String> sort) {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;

        pagination = new Pagination(page, size);
        sorting = WebSorting.toSorting(sort);

        tokens = service.getAll(pagination, sorting);

        return UserTokenDtoMapper.toResponseDto(tokens);
    }

    @Override
    @RequireResourceAuthorization(resource = "USER_TOKEN", action = Actions.READ)
    public UserTokenResponseDto getOneUserToken(final String token) {
        final Optional<UserToken> userToken;

        userToken = service.getOne(token);

        return UserTokenDtoMapper.toResponseDto(userToken);
    }

    @Override
    @RequireResourceAuthorization(resource = "USER_TOKEN", action = Actions.UPDATE)
    public UserTokenResponseDto patchUserToken(final String token, @Valid final UserTokenChangeDto userTokenChangeDto) {
        final UserToken toUpdate;
        final UserToken updated;

        toUpdate = UserTokenDtoMapper.toDomain(userTokenChangeDto, token);
        updated = service.patch(toUpdate);
        return UserTokenDtoMapper.toResponseDto(updated);
    }

}
