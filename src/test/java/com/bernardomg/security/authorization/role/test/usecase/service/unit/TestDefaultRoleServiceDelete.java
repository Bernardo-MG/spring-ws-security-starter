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

package com.bernardomg.security.authorization.role.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.usecase.service.DefaultRoleService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - delete")
class TestDefaultRoleServiceDelete {

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultRoleService service;

    @Mock
    private UserRoleRepository userRoleRepository;

    public TestDefaultRoleServiceDelete() {
        super();
    }

    @Test
    @DisplayName("Deleting calls the repository")
    void testDelete() {

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        service.delete(RoleConstants.NAME);

        // THEN
        verify(roleRepository).delete(RoleConstants.NAME);
    }

    @Test
    @DisplayName("Deleting a not existing role throws an exception")
    @ValidUser
    void testDelete_NotExisting() {
        final ThrowingCallable executable;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        executable = () -> service.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

}
