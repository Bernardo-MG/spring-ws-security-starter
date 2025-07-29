
package com.bernardomg.security.role.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.role.usecase.service.DefaultRoleService;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - create")
class TestDefaultRoleServiceCreate {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultRoleService           service;

    public TestDefaultRoleServiceCreate() {
        super();
    }

    @Test
    @DisplayName("When the role name is empty, an exception is thrown")
    void testCreate_NameEmpty() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // WHEN
        executable = () -> service.create("");

        // THEN
        failure = new FieldFailure("empty", "name", "name.empty", "");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("When the role name already exists, an exception is thrown")
    void testCreate_NameExists() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        executable = () -> service.create(RoleConstants.NAME);

        // THEN
        failure = new FieldFailure("existing", "name", "name.existing", RoleConstants.NAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Sends the role to the repository")
    void testCreate_PersistedData() {
        // WHEN
        service.create(RoleConstants.NAME);

        // THEN
        verify(roleRepository).save(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns the created role")
    void testCreate_ReturnedData() {
        final Role result;

        // GIVEN
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withoutPermissions());

        // WHEN
        result = service.create(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Roles.withoutPermissions());
    }

}
