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

package com.bernardomg.security.authentication.user.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.authentication.user.cache.UserCaches;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserQueryRequest;
import com.bernardomg.security.authentication.user.model.query.UserRegisterRequest;
import com.bernardomg.security.authentication.user.model.query.UserUpdateRequest;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authorization.permission.constant.Actions;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * User REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/user")
@AllArgsConstructor
@Transactional
public class UserController {

    /**
     * Service which handles user activation.
     */
    private final UserActivationService userActivationService;

    /**
     * Service which handles user queries.
     */
    private final UserQueryService      userQueryService;

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
    public User create(@Valid @RequestBody final UserRegisterRequest request) {
        return userActivationService.registerNewUser(request.getUsername(), request.getName(), request.getEmail());
    }

    /**
     * Deletes a user by its id.
     *
     * @param username
     *            username of the user to delete
     */
    @DeleteMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.DELETE)
    @Caching(evict = { @CacheEvict(cacheNames = UserCaches.USERS, allEntries = true),
            @CacheEvict(cacheNames = UserCaches.USER) })
    public void delete(@PathVariable("username") final String username) {
        userQueryService.delete(username);
    }

    /**
     * Returns all the users in a paginated form.
     *
     * @param user
     *            query to filter users
     * @param page
     *            pagination to apply
     * @return a page for the users matching the sample
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.USERS)
    public Iterable<User> readAll(@Valid final UserQueryRequest user, final Pageable page) {
        return userQueryService.getAll(user, page);
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
        return userQueryService.getOne(username)
            .orElse(null);
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
    @Caching(put = { @CachePut(cacheNames = UserCaches.USER, key = "#result.username") },
            evict = { @CacheEvict(cacheNames = UserCaches.USERS, allEntries = true) })
    public User update(@PathVariable("username") final String username,
            @Valid @RequestBody final UserUpdateRequest request) {
        return userQueryService.update(username, request);
    }

}
