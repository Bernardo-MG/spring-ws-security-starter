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

package com.bernardomg.security.role.adapter.outbound.rest.controller;

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
import com.bernardomg.security.openapi.api.RoleApi;
import com.bernardomg.security.openapi.model.ResourcePermissionPageResponseDto;
import com.bernardomg.security.openapi.model.RoleChangeDto;
import com.bernardomg.security.openapi.model.RoleCreationDto;
import com.bernardomg.security.openapi.model.RolePageResponseDto;
import com.bernardomg.security.openapi.model.RoleResponseDto;
import com.bernardomg.security.permission.data.constant.Actions;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.adapter.outbound.cache.RoleCaches;
import com.bernardomg.security.role.adapter.outbound.rest.model.RoleDtoMapper;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;
import com.bernardomg.security.role.usecase.service.RoleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

/**
 * Role REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
public class RoleController implements RoleApi {

    /**
     * Role service.
     */
    private final RoleService service;

    public RoleController(final RoleService service) {
        super();

        this.service = service;
    }

    @Override
    @RequireResourceAccess(resource = "ROLE", action = Actions.CREATE)
    @Caching(put = { @CachePut(cacheNames = RoleCaches.ROLE, key = "#result.content.name") },
            evict = { @CacheEvict(cacheNames = RoleCaches.ROLES, allEntries = true) })
    public RoleResponseDto createRole(@Valid final RoleCreationDto roleCreationDto) {
        final Role role;

        role = service.create(roleCreationDto.getName());

        return RoleDtoMapper.toResponseDto(role);
    }

    @Override
    @RequireResourceAccess(resource = "ROLE", action = Actions.DELETE)
    @Caching(evict = { @CacheEvict(cacheNames = RoleCaches.ROLE),
            @CacheEvict(cacheNames = { RoleCaches.ROLES, RoleCaches.ROLE_AVAILABLE_PERMISSIONS }, allEntries = true) })
    public RoleResponseDto deleteRole(final String name) {
        final Role deleted;

        deleted = service.delete(name);

        return RoleDtoMapper.toResponseDto(deleted);
    }

    @Override
    @RequireResourceAccess(resource = "ROLE", action = Actions.READ)
    @Cacheable(cacheNames = RoleCaches.ROLE_AVAILABLE_PERMISSIONS)
    public ResourcePermissionPageResponseDto getAllRolePermissions(@Valid final String role,
            @Min(1) @Valid final Integer page, @Min(1) @Valid final Integer size, @Valid final List<String> sort) {
        final Pagination               pagination;
        final Sorting                  sorting;
        final Page<ResourcePermission> permissions;

        pagination = new Pagination(page, size);
        sorting = WebSorting.toSorting(sort);

        permissions = service.getAvailablePermissions(role, pagination, sorting);

        return RoleDtoMapper.toPermissionResponseDto(permissions);
    }

    @Override
    @RequireResourceAccess(resource = "ROLE", action = Actions.READ)
    @Cacheable(cacheNames = RoleCaches.ROLES)
    public RolePageResponseDto getAllRoles(String name, @Min(1) @Valid Integer page, @Min(1) @Valid Integer size,
            @Valid List<String> sort) {
        final Pagination pagination;
        final Sorting    sorting;
        final Page<Role> fees;
        final RoleQuery  query;

        pagination = new Pagination(page, size);
        sorting = WebSorting.toSorting(sort);

        query = new RoleQuery(name);
        fees = service.getAll(query, pagination, sorting);

        return RoleDtoMapper.toResponseDto(fees);
    }

    @Override
    @RequireResourceAccess(resource = "ROLE", action = Actions.READ)
    @Cacheable(cacheNames = RoleCaches.ROLE)
    public RoleResponseDto getOneRole(final String role) {
        final Optional<Role> found;

        found = service.getOne(role);

        return RoleDtoMapper.toResponseDto(found);
    }

    @Override
    @RequireResourceAccess(resource = "ROLE", action = Actions.UPDATE)
    @Caching(put = { @CachePut(cacheNames = RoleCaches.ROLE, key = "#result.content.name") }, evict = {
            @CacheEvict(cacheNames = { RoleCaches.ROLES, RoleCaches.ROLE_AVAILABLE_PERMISSIONS }, allEntries = true) })
    public RoleResponseDto updateRole(final String name, @Valid final RoleChangeDto roleChangeDto) {
        final Role toUpdate;
        final Role updated;

        toUpdate = RoleDtoMapper.toDomain(roleChangeDto, name);
        updated = service.update(toUpdate);
        return RoleDtoMapper.toResponseDto(updated);
    }

}
