
package com.bernardomg.security.authorization.permission.test.adapter.inbound.spring.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.permission.adapter.inbound.spring.PersistentUserDetailsService;
import com.bernardomg.security.authorization.permission.domain.repository.UserPermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersistentUserDetailsService")
class TestPersistentUserDetailsService {

    @InjectMocks
    private PersistentUserDetailsService service;

    @Mock
    private UserPermissionRepository     userPermissionRepository;

    @Mock
    private UserRepository               userRepository;

    public TestPersistentUserDetailsService() {
        super();
    }

    @Test
    @DisplayName("When the user is disabled it is returned")
    void testLoadByUsername_Disabled() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.disabled()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));
        given(userPermissionRepository.findAll(UserConstants.USERNAME))
            .willReturn(List.of(ResourcePermissions.read()));

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userDetails.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(userDetails.getPassword())
                .as("password")
                .isEqualTo(UserConstants.PASSWORD);
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
                .isFalse();

            softly.assertThat(userDetails.getAuthorities())
                .as("authorities size")
                .hasSize(1);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("resource")
                .first()
                .as("authority resource")
                .isEqualTo(PermissionConstants.DATA);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("action")
                .first()
                .as("authority action")
                .isEqualTo(PermissionConstants.READ);
        });
    }

    @Test
    @DisplayName("When the user is enabled it is returned")
    void testLoadByUsername_Enabled() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));
        given(userPermissionRepository.findAll(UserConstants.USERNAME))
            .willReturn(List.of(ResourcePermissions.read()));

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userDetails.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(userDetails.getPassword())
                .as("password")
                .isEqualTo(UserConstants.PASSWORD);
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
                .hasSize(1);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("resource")
                .first()
                .as("authority resource")
                .isEqualTo(PermissionConstants.DATA);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("action")
                .first()
                .as("authority action")
                .isEqualTo(PermissionConstants.READ);
        });
    }

    @Test
    @DisplayName("When the user is expired it is returned")
    void testLoadByUsername_Expired() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.expired()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));
        given(userPermissionRepository.findAll(UserConstants.USERNAME))
            .willReturn(List.of(ResourcePermissions.read()));

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userDetails.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(userDetails.getPassword())
                .as("password")
                .isEqualTo(UserConstants.PASSWORD);
            softly.assertThat(userDetails.isAccountNonExpired())
                .as("non expired")
                .isFalse();
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
                .hasSize(1);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("resource")
                .first()
                .as("authority resource")
                .isEqualTo(PermissionConstants.DATA);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("action")
                .first()
                .as("authority action")
                .isEqualTo(PermissionConstants.READ);
        });
    }

    @Test
    @DisplayName("When the user is locked it is returned")
    void testLoadByUsername_Locked() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.locked()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));
        given(userPermissionRepository.findAll(UserConstants.USERNAME))
            .willReturn(List.of(ResourcePermissions.read()));

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userDetails.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(userDetails.getPassword())
                .as("password")
                .isEqualTo(UserConstants.PASSWORD);
            softly.assertThat(userDetails.isAccountNonExpired())
                .as("non expired")
                .isTrue();
            softly.assertThat(userDetails.isAccountNonLocked())
                .as("non locked")
                .isFalse();
            softly.assertThat(userDetails.isCredentialsNonExpired())
                .as("credentials non expired")
                .isTrue();
            softly.assertThat(userDetails.isEnabled())
                .as("enabled")
                .isTrue();

            softly.assertThat(userDetails.getAuthorities())
                .as("authorities size")
                .hasSize(1);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("resource")
                .first()
                .as("authority resource")
                .isEqualTo(PermissionConstants.DATA);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("action")
                .first()
                .as("authority action")
                .isEqualTo(PermissionConstants.READ);
        });
    }

    @Test
    @DisplayName("When the user doesn't have authorities an exception is thrown")
    void testLoadByUsername_NoAuthorities() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userPermissionRepository.findAll(UserConstants.USERNAME)).willReturn(List.of());

        // WHEN
        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, UsernameNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username " + UserConstants.USERNAME + " has no authorities");
    }

    @Test
    @DisplayName("When the user has the password expired it is returned")
    void testLoadByUsername_PasswordExpired() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpired()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));
        given(userPermissionRepository.findAll(UserConstants.USERNAME))
            .willReturn(List.of(ResourcePermissions.read()));

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userDetails.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(userDetails.getPassword())
                .as("password")
                .isEqualTo(UserConstants.PASSWORD);
            softly.assertThat(userDetails.isAccountNonExpired())
                .as("non expired")
                .isTrue();
            softly.assertThat(userDetails.isAccountNonLocked())
                .as("non locked")
                .isTrue();
            softly.assertThat(userDetails.isCredentialsNonExpired())
                .as("credentials non expired")
                .isFalse();
            softly.assertThat(userDetails.isEnabled())
                .as("enabled")
                .isTrue();

            softly.assertThat(userDetails.getAuthorities())
                .as("authorities size")
                .hasSize(1);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("resource")
                .first()
                .as("authority resource")
                .isEqualTo(PermissionConstants.DATA);
            softly.assertThat(userDetails.getAuthorities())
                .extracting("action")
                .first()
                .as("authority action")
                .isEqualTo(PermissionConstants.READ);
        });
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testLoadByUsername_UserNotExisting() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, UsernameNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username " + UserConstants.USERNAME + " not found in database");
    }

}
