
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
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
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
    private RolePermissionRepository     rolePermissionRepository;

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
        final Role             toCreate;

        // GIVEN
        toCreate = Roles.noName();

        // WHEN
        executable = () -> service.create(toCreate);

        // THEN
        failure = new FieldFailure("empty", "name", "name.empty", "");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("When the role name already exists, an exception is thrown")
    void testCreate_NameExists() {
        final ThrowingCallable executable;
        final FieldFailure     failure;
        final Role             toCreate;

        // GIVEN
        toCreate = Roles.withoutPermissions();

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        executable = () -> service.create(toCreate);

        // THEN
        failure = new FieldFailure("existing", "name", "name.existing", RoleConstants.NAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Sends a role without permissions to the repository")
    void testCreate_NoPermissions_PersistedData() {
        final Role toCreate;

        // GIVEN
        toCreate = Roles.withoutPermissions();

        // WHEN
        service.create(toCreate);

        // THEN
        verify(roleRepository).save(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns the created role without permissions")
    void testCreate_NoPermissions_ReturnedData() {
        final Role result;
        final Role toCreate;

        // GIVEN
        toCreate = Roles.withoutPermissions();

        // GIVEN
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withoutPermissions());

        // WHEN
        result = service.create(toCreate);

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Sends a role with permissions to the repository")
    void testCreate_Permissions_PersistedData() {
        final Role toCreate;

        // GIVEN
        toCreate = Roles.withSinglePermission();

        // WHEN
        service.create(toCreate);

        // THEN
        verify(roleRepository).save(Roles.withSinglePermission());
    }

    @Test
    @DisplayName("Returns the created role with permissions")
    void testCreate_Permissions_ReturnedData() {
        final Role result;
        final Role toCreate;

        // GIVEN
        toCreate = Roles.withSinglePermission();

        // GIVEN
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withSinglePermission());

        // WHEN
        result = service.create(toCreate);

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Roles.withSinglePermission());
    }

}
