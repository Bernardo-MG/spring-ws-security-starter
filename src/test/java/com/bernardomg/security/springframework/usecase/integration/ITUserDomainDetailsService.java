
package com.bernardomg.security.springframework.usecase.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bernardomg.security.permission.test.config.annotation.AlternativeUserWithCrudPermissions;
import com.bernardomg.security.permission.test.config.annotation.UserWithCrudPermissions;
import com.bernardomg.security.permission.test.config.annotation.UserWithCrudPermissionsNotGranted;
import com.bernardomg.security.permission.test.config.annotation.UserWithoutPermissions;
import com.bernardomg.security.springframework.usecase.UserDomainDetailsService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

/**
 * TODO: why this test class? The unit test seem to cover everything
 */
@IntegrationTest
@DisplayName("UserDomainDetailsService - integration")
class ITUserDomainDetailsService {

    @Autowired
    private UserDomainDetailsService service;

    public ITUserDomainDetailsService() {
        super();
    }

    @Test
    @DisplayName("When the user is enabled it is returned")
    @UserWithCrudPermissions
    void testLoadByUsername_Enabled() {
        final UserDetails userDetails;

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userDetails.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(userDetails.getPassword())
                .as("password")
                .isEqualTo(UserConstants.ENCODED_PASSWORD);
            softly.assertThat(userDetails.isAccountNonExpired())
                .as("non expired")
                .isTrue();
            softly.assertThat(userDetails.isAccountNonLocked())
                .as("non locked")
                .isTrue();
            softly.assertThat(userDetails.isCredentialsNonExpired())
                .as("credentials non expired")
                .isTrue();
            softly.assertThat(userDetails.isEnabled())
                .as("enabled")
                .isTrue();

            softly.assertThat(userDetails.getAuthorities())
                .as("authorities size")
                .hasSize(4);
        });
    }

    @Test
    @DisplayName("When the user has no granted permissions an exception is thrown")
    @UserWithCrudPermissionsNotGranted
    void testLoadByUsername_NoGrantedPermissions() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        exception = Assertions.catchThrowableOfType(UsernameNotFoundException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username " + UserConstants.USERNAME + " has no authorities");
    }

    @Test
    @DisplayName("When the user has no granted permissions, and there is another user with permissions, an exception is thrown")
    @UserWithCrudPermissionsNotGranted
    @AlternativeUserWithCrudPermissions
    void testLoadByUsername_NoGrantedPermissions_AlternativeUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        exception = Assertions.catchThrowableOfType(UsernameNotFoundException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username " + UserConstants.USERNAME + " has no authorities");
    }

    @Test
    @DisplayName("When the user has no permissions an exception is thrown")
    @UserWithoutPermissions
    void testLoadByUsername_NoPermissions() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        exception = Assertions.catchThrowableOfType(UsernameNotFoundException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username " + UserConstants.USERNAME + " has no authorities");
    }

}
