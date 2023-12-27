
package com.bernardomg.security.authorization.springframework.test.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.AlternativeUserWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.UserWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.UserWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.UserWithoutPermissions;
import com.bernardomg.security.authorization.springframework.PersistentUserDetailsService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserDetailsService - integration")
class ITPersistentUserDetailsService {

    @Autowired
    private PersistentUserDetailsService service;

    public ITPersistentUserDetailsService() {
        super();
    }

    @Test
    @DisplayName("When the user is enabled it is returned")
    @UserWithCrudPermissions
    void testLoadByUsername_Enabled() {
        final UserDetails userDetails;

        userDetails = service.loadUserByUsername(UserConstants.USERNAME);

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

        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        exception = Assertions.catchThrowableOfType(executable, UsernameNotFoundException.class);

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

        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        exception = Assertions.catchThrowableOfType(executable, UsernameNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username " + UserConstants.USERNAME + " has no authorities");
    }

    @Test
    @DisplayName("When the user has no permissions an exception is thrown")
    @UserWithoutPermissions
    void testLoadByUsername_NoPermissions() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        exception = Assertions.catchThrowableOfType(executable, UsernameNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username " + UserConstants.USERNAME + " has no authorities");
    }

}
