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

package com.bernardomg.security.user.data.adapter.outbound.rest.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.permission.data.constant.Actions;
import com.bernardomg.security.role.adapter.outbound.cache.RoleCaches;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.data.adapter.outbound.cache.UserCaches;
import com.bernardomg.security.user.data.adapter.outbound.rest.model.NewUser;
import com.bernardomg.security.user.data.adapter.outbound.rest.model.UserChange;
import com.bernardomg.security.user.data.adapter.outbound.rest.model.UserQueryRequest;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.model.UserQuery;
import com.bernardomg.security.user.data.usecase.service.UserService;

/**
 * User REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/user")
public class UserController {

    /**
     * Service which handles user queries.
     */
    private final UserService service;

    public UserController(final UserService service) {
        super();

        this.service = service;
    }

    /**
     * Deletes a user by its id.
     *
     * @param username
     *            username of the user to delete
     */
    @DeleteMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.DELETE)
    @Caching(evict = { @CacheEvict(cacheNames = UserCaches.USER),
            @CacheEvict(cacheNames = { UserCaches.USERS, RoleCaches.USER_AVAILABLE_ROLES }, allEntries = true) })
    public void delete(@PathVariable("username") final String username) {
        service.delete(username);
    }

    /**
     * Returns all the users in a paginated form.
     *
     * @param request
     *            query to filter users
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return a page for the users matching the sample
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.USERS)
    public Page<User> readAll(final UserQueryRequest request, final Pagination pagination, final Sorting sorting) {
        final UserQuery query;

        query = new UserQuery(request.email(), request.username(), request.name(), request.enabled(),
            request.notExpired(), request.notLocked(), request.passwordNotExpired());
        return service.getAll(query, pagination, sorting);
    }

    /**
     * Reads a single user by its id.
     *
     * @param username
     *            username of the user to read
     * @return the user for the id, or {@code null} if it doesn't exist
     */
    @GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.USER)
    public User readOne(@PathVariable("username") final String username) {
        // TODO: maybe optionals must be unwrapped automatically
        return service.getOne(username)
            .orElse(null);
    }

    /**
     * Creates a user.
     *
     * @param request
     *            user to add
     * @return the new user
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @RequireResourceAccess(resource = "USER", action = Actions.CREATE)
    @Caching(put = { @CachePut(cacheNames = UserCaches.USER, key = "#result.username") },
            evict = { @CacheEvict(cacheNames = UserCaches.USERS, allEntries = true) })
    public User registerNewUser(@RequestBody final NewUser request) {
        return service.registerNewUser(request.username(), request.name(), request.email());
    }

    /**
     * Updates a user.
     *
     * @param username
     *            username of the user to update
     * @param request
     *            updated user data
     * @return the updated user
     */
    @PutMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.UPDATE)
    @Caching(put = { @CachePut(cacheNames = UserCaches.USER, key = "#result.username") }, evict = {
            @CacheEvict(cacheNames = { UserCaches.USERS, RoleCaches.USER_AVAILABLE_ROLES }, allEntries = true) })
    public User update(@PathVariable("username") final String username, @RequestBody final UserChange request) {
        final User             user;
        final Collection<Role> roles;

        if (request.roles() == null) {
            roles = List.of();
        } else {
            roles = request.roles()
                .stream()
                .map(r -> new Role(r, List.of()))
                .toList();
        }
        user = new User(request.email(), username, request.name(), request.enabled(), request.passwordNotExpired(),
            false, false, roles);

        return service.update(user);
    }

}
