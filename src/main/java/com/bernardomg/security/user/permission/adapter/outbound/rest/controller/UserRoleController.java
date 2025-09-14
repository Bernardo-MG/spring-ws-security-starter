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

package com.bernardomg.security.user.permission.adapter.outbound.rest.controller;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
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
import com.bernardomg.security.user.permission.usecase.service.UserRoleService;
import com.bernardomg.ucronia.openapi.api.UserRoleApi;
import com.bernardomg.ucronia.openapi.model.RolePageResponseDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

/**
 * User role REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class UserRoleController implements UserRoleApi {

    /**
     * User role service.
     */
    private final UserRoleService service;

    public UserRoleController(final UserRoleService service) {
        super();

        this.service = service;
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

}
