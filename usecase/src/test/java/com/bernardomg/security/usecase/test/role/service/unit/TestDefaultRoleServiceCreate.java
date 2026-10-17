
package com.bernardomg.security.usecase.test.role.service.unit;

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

import com.bernardomg.security.domain.permission.exception.MissingResourcePermissionException;
import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.usecase.role.service.DefaultRoleService;
import com.bernardomg.security.usecase.test.role.config.factory.PermissionConstants;
import com.bernardomg.security.usecase.test.role.config.factory.RoleConstants;
import com.bernardomg.security.usecase.test.role.config.factory.Roles;
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

    @Test
    @DisplayName("When the role name is empty, then a validation exception is thrown")
    void testCreate_NameEmpty() {
        final ThrowingCallable action;
        final FieldFailure     failure;
        final Role             toCreate;

        // GIVEN
        toCreate = Roles.noName();

        // WHEN
        action = () -> service.create(toCreate);

        // THEN
        failure = new FieldFailure("empty", "name", "name.empty", "");

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When the role name already exists, then a validation exception is thrown")
    void testCreate_NameExists() {
        final ThrowingCallable action;
        final FieldFailure     failure;
        final Role             toCreate;

        // GIVEN
        toCreate = Roles.withoutPermissions();

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        action = () -> service.create(toCreate);

        // THEN
        failure = new FieldFailure("existing", "name", "name.existing", RoleConstants.NAME);

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When creating a role without permissions, then it is sent to the repository")
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
    @DisplayName("When creating a role without permissions, then the created role is returned")
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
            .as("When creating a role without permissions, then the created role is returned (result)")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When creating a role with permissions, then it is sent to the repository")
    void testCreate_Permissions_PersistedData() {
        final Role toCreate;

        // GIVEN
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.CREATE))
            .willReturn(true);
        toCreate = Roles.withSinglePermission();

        // WHEN
        service.create(toCreate);

        // THEN
        verify(roleRepository).save(Roles.withSinglePermission());
    }

    @Test
    @DisplayName("When creating a role with permissions, then the created role is returned")
    void testCreate_Permissions_ReturnedData() {
        final Role result;
        final Role toCreate;

        // GIVEN
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.CREATE))
            .willReturn(true);
        toCreate = Roles.withSinglePermission();

        // GIVEN
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withSinglePermission());

        // WHEN
        result = service.create(toCreate);

        // THEN
        Assertions.assertThat(result)
            .as("When creating a role with permissions, then the created role is returned (result)")
            .isEqualTo(Roles.withSinglePermission());
    }

    @Test
    @DisplayName("When a permission does not exist, then an exception is thrown")
    void testUpdate_NotExistingPermission() {
        final ThrowingCallable action;
        final Role             data;

        // GIVEN
        data = Roles.withSinglePermission();

        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.CREATE))
            .willReturn(false);

        // WHEN
        action = () -> service.create(data);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When a permission does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingResourcePermissionException.class);
    }

}
