
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.test.config.role.factory.RoleConstants;
import com.bernardomg.security.domain.role.repository.RoleRepository;

@IntegrationTest
@DisplayName("RoleRepository - exists")
class ITRoleRepositoryExists {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryExists() {
        super();
    }

    @Test
    @DisplayName("When the role exists it is returned as existing")
    @RoleWithoutPermissions
    void testExists() {
        final boolean exists;

        // WHEN
        exists = repository.exists(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isTrue();
    }

    @Test
    @DisplayName("When the role exists, ignoring case, it is returned as existing")
    @RoleWithoutPermissions
    void testExists_IgnoreCase() {
        final boolean exists;

        // WHEN
        exists = repository.exists(RoleConstants.NAME.toUpperCase(LocaleContextHolder.getLocale()));

        // THEN
        Assertions.assertThat(exists)
            .isTrue();
    }

    @Test
    @DisplayName("When there is no data it is returned as not existing")
    void testExists_NotData() {
        final boolean exists;

        // WHEN
        exists = repository.exists(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When the role doesn't exists it is returned as not existing")
    @RoleWithoutPermissions
    void testExists_NotExisting() {
        final boolean exists;

        // WHEN
        exists = repository.exists("abc");

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

}
