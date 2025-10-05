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

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.web.WebSorting;
import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.permission.data.constant.Actions;
import com.bernardomg.security.role.adapter.outbound.cache.RoleCaches;
import com.bernardomg.security.role.adapter.outbound.rest.model.RoleDtoMapper;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.adapter.outbound.cache.UserCaches;
import com.bernardomg.security.user.adapter.outbound.rest.model.UserDtoMapper;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserQuery;
import com.bernardomg.security.user.usecase.service.UserService;
import com.bernardomg.ucronia.openapi.api.UserApi;
import com.bernardomg.ucronia.openapi.model.RolePageResponseDto;
import com.bernardomg.ucronia.openapi.model.UserChangeDto;
import com.bernardomg.ucronia.openapi.model.UserCreationDto;
import com.bernardomg.ucronia.openapi.model.UserPageResponseDto;
import com.bernardomg.ucronia.openapi.model.UserResponseDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

/**
 * User REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class UserController implements UserApi {

    /**
     * Service which handles user queries.
     */
    private final UserService service;

    public UserController(final UserService service) {
        super();

        this.service = service;
    }

    @Override
    @RequireResourceAccess(resource = "USER", action = Actions.CREATE)
    @Caching(put = { @CachePut(cacheNames = UserCaches.USER, key = "#result.content.username") },
            evict = { @CacheEvict(cacheNames = UserCaches.USERS, allEntries = true) })
    public UserResponseDto createUser(@Valid final UserCreationDto userCreationDto) {
        final User user;

        user = service.registerNewUser(userCreationDto.getUsername(), userCreationDto.getName(),
            userCreationDto.getEmail());

        return UserDtoMapper.toResponseDto(user);
    }

    @Override
    @RequireResourceAccess(resource = "USER", action = Actions.DELETE)
    @Caching(evict = { @CacheEvict(cacheNames = UserCaches.USER),
            @CacheEvict(cacheNames = { UserCaches.USERS, RoleCaches.USER_AVAILABLE_ROLES }, allEntries = true) })
    public UserResponseDto deleteUser(final String username) {
        final User deleted;

        deleted = service.delete(username);

        return UserDtoMapper.toResponseDto(deleted);
    }

    @Override
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.USERS)
    public UserPageResponseDto getAllUsers(@Min(1) @Valid final Integer page, @Min(1) @Valid final Integer size,
            @Valid final List<String> sort, @Email @Valid final String email, @Valid final String username,
            @Valid final String name, @Valid final Boolean enabled, @Valid final Boolean notLocked,
            @Valid final Boolean notExpired, @Valid final Boolean passwordNotExpired) {
        final Pagination pagination;
        final Sorting    sorting;
        final Page<User> users;
        final UserQuery  query;

        pagination = new Pagination(page, size);
        sorting = WebSorting.toSorting(sort);

        query = new UserQuery(email, username, name, enabled, notLocked, notExpired, passwordNotExpired);
        users = service.getAll(query, pagination, sorting);

        return UserDtoMapper.toResponseDto(users);
    }

    @Override
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = RoleCaches.USER_AVAILABLE_ROLES)
    public RolePageResponseDto getAvailableRolesForUser(final String username, @Min(1) @Valid final Integer page,
            @Min(1) @Valid final Integer size, @Valid final List<String> sort) {
        final Pagination pagination;
        final Sorting    sorting;
        Page<Role>       roles;

        pagination = new Pagination(page, size);
        sorting = WebSorting.toSorting(sort);
        roles = service.getAvailableRoles(username, pagination, sorting);
        return RoleDtoMapper.toResponseDto(roles);
    }

    @Override
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.USER)
    public UserResponseDto getOneUser(final String user) {
        final Optional<User> found;

        found = service.getOne(user);

        return UserDtoMapper.toResponseDto(found);
    }

    @Override
    @RequireResourceAccess(resource = "USER", action = Actions.UPDATE)
    @Caching(put = { @CachePut(cacheNames = UserCaches.USER, key = "#result.content.username") }, evict = {
            @CacheEvict(cacheNames = { UserCaches.USERS, RoleCaches.USER_AVAILABLE_ROLES }, allEntries = true) })
    public UserResponseDto updateUser(final String username, @Valid final UserChangeDto userChangeDto) {
        final User toUpdate;
        final User updated;

        toUpdate = UserDtoMapper.toDomain(userChangeDto, username);
        updated = service.update(toUpdate);
        return UserDtoMapper.toResponseDto(updated);
    }

}
