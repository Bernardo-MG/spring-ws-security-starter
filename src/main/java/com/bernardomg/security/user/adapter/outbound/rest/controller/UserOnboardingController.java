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

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.Unsecured;
import com.bernardomg.security.user.adapter.outbound.cache.UserCaches;
import com.bernardomg.security.user.adapter.outbound.rest.model.UserActivationDtoMapper;
import com.bernardomg.security.user.adapter.outbound.rest.model.UserTokenDtoMapper;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserTokenStatus;
import com.bernardomg.security.user.usecase.service.UserOnboardingService;
import com.bernardomg.ucronia.openapi.api.UserOnboardingApi;
import com.bernardomg.ucronia.openapi.model.UserActivationDto;
import com.bernardomg.ucronia.openapi.model.UserCreationDto;
import com.bernardomg.ucronia.openapi.model.UserResponseDto;
import com.bernardomg.ucronia.openapi.model.UserTokenStatusResponseDto;

import jakarta.validation.Valid;

/**
 * User activation REST controller. This requires a token, given to the new user when he registers.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class UserOnboardingController implements UserOnboardingApi {

    /**
     * Service which handles user activation.
     */
    private final UserOnboardingService service;

    public UserOnboardingController(final UserOnboardingService service) {
        super();

        this.service = service;
    }

    @Override
    @Unsecured
    @Caching(put = { @CachePut(cacheNames = UserCaches.USER, key = "#result.content.username") },
            evict = { @CacheEvict(cacheNames = UserCaches.USERS, allEntries = true) })
    public UserResponseDto activateUser(final String token, @Valid final UserActivationDto userActivationDto) {
        final User user;

        user = service.activateUser(token, userActivationDto.getPassword());
        return UserActivationDtoMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto inviteUser(@Valid final UserCreationDto userCreationDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Unsecured
    public UserTokenStatusResponseDto validateActivationToken(final String token) {
        final UserTokenStatus userToken;

        userToken = service.validateToken(token);
        return UserTokenDtoMapper.toResponseDto(userToken);
    }

}
