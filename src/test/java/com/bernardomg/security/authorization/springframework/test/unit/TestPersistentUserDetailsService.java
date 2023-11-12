
package com.bernardomg.security.authorization.springframework.test.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authorization.permission.persistence.model.PersistentUserGrantedPermission;
import com.bernardomg.security.authorization.permission.persistence.repository.UserGrantedPermissionRepository;
import com.bernardomg.security.authorization.springframework.PersistentUserDetailsService;
import com.bernardomg.security.user.test.util.model.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersistentUserDetailsService")
class TestPersistentUserDetailsService {

    @InjectMocks
    private PersistentUserDetailsService    service;

    @Mock
    private UserGrantedPermissionRepository userGrantedPermissionRepository;

    @Mock
    private UserRepository                  userRepository;

    public TestPersistentUserDetailsService() {
        super();
    }

    private final PersistentUserGrantedPermission getPersistentPermission() {
        return PersistentUserGrantedPermission.builder()
            .resource("resource")
            .action("action")
            .build();
    }

    @Test
    @DisplayName("When the user is disabled it is returned")
    void testLoadByUsername_Disabled() {
        final UserDetails    userDetails;
        final PersistentUser user;

        user = Users.disabled();
        given(userRepository.findOneByUsername("username")).willReturn(Optional.of(user));
        given(userGrantedPermissionRepository.findAllByUserId(1L)).willReturn(List.of(getPersistentPermission()));

        userDetails = service.loadUserByUsername("username");

        Assertions.assertThat(userDetails.getUsername())
            .as("username")
            .isEqualTo("username");
        Assertions.assertThat(userDetails.getPassword())
            .as("password")
            .isEqualTo("1234");
        Assertions.assertThat(userDetails.isAccountNonExpired())
            .as("non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isAccountNonLocked())
            .as("non locked")
            .isTrue();
        Assertions.assertThat(userDetails.isCredentialsNonExpired())
            .as("credentials non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isEnabled())
            .as("enabled")
            .isFalse();

        Assertions.assertThat(userDetails.getAuthorities())
            .as("authorities size")
            .hasSize(1);
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("resource")
            .first()
            .as("authority resource")
            .isEqualTo("resource");
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("action")
            .first()
            .as("authority action")
            .isEqualTo("action");
    }

    @Test
    @DisplayName("When the user is enabled it is returned")
    void testLoadByUsername_Enabled() {
        final UserDetails    userDetails;
        final PersistentUser user;

        user = Users.enabled();
        given(userRepository.findOneByUsername("username")).willReturn(Optional.of(user));
        given(userGrantedPermissionRepository.findAllByUserId(1L)).willReturn(List.of(getPersistentPermission()));

        userDetails = service.loadUserByUsername("username");

        Assertions.assertThat(userDetails.getUsername())
            .as("username")
            .isEqualTo("username");
        Assertions.assertThat(userDetails.getPassword())
            .as("password")
            .isEqualTo("1234");
        Assertions.assertThat(userDetails.isAccountNonExpired())
            .as("non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isAccountNonLocked())
            .as("non locked")
            .isTrue();
        Assertions.assertThat(userDetails.isCredentialsNonExpired())
            .as("credentials non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isEnabled())
            .as("enabled")
            .isTrue();

        Assertions.assertThat(userDetails.getAuthorities())
            .as("authorities size")
            .hasSize(1);
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("resource")
            .first()
            .as("authority resource")
            .isEqualTo("resource");
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("action")
            .first()
            .as("authority action")
            .isEqualTo("action");
    }

    @Test
    @DisplayName("When the user is expired it is returned")
    void testLoadByUsername_Expired() {
        final UserDetails    userDetails;
        final PersistentUser user;

        user = Users.expired();
        given(userRepository.findOneByUsername("username")).willReturn(Optional.of(user));
        given(userGrantedPermissionRepository.findAllByUserId(1L)).willReturn(List.of(getPersistentPermission()));

        userDetails = service.loadUserByUsername("username");

        Assertions.assertThat(userDetails.getUsername())
            .as("username")
            .isEqualTo("username");
        Assertions.assertThat(userDetails.getPassword())
            .as("password")
            .isEqualTo("1234");
        Assertions.assertThat(userDetails.isAccountNonExpired())
            .as("non expired")
            .isFalse();
        Assertions.assertThat(userDetails.isAccountNonLocked())
            .as("non locked")
            .isTrue();
        Assertions.assertThat(userDetails.isCredentialsNonExpired())
            .as("credentials non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isEnabled())
            .as("enabled")
            .isTrue();

        Assertions.assertThat(userDetails.getAuthorities())
            .as("authorities size")
            .hasSize(1);
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("resource")
            .first()
            .as("authority resource")
            .isEqualTo("resource");
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("action")
            .first()
            .as("authority action")
            .isEqualTo("action");
    }

    @Test
    @DisplayName("When the user is locked it is returned")
    void testLoadByUsername_Locked() {
        final UserDetails    userDetails;
        final PersistentUser user;

        user = Users.locked();
        given(userRepository.findOneByUsername("username")).willReturn(Optional.of(user));
        given(userGrantedPermissionRepository.findAllByUserId(1L)).willReturn(List.of(getPersistentPermission()));

        userDetails = service.loadUserByUsername("username");

        Assertions.assertThat(userDetails.getUsername())
            .as("username")
            .isEqualTo("username");
        Assertions.assertThat(userDetails.getPassword())
            .as("password")
            .isEqualTo("1234");
        Assertions.assertThat(userDetails.isAccountNonExpired())
            .as("non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isAccountNonLocked())
            .as("non locked")
            .isFalse();
        Assertions.assertThat(userDetails.isCredentialsNonExpired())
            .as("credentials non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isEnabled())
            .as("enabled")
            .isTrue();

        Assertions.assertThat(userDetails.getAuthorities())
            .as("authorities size")
            .hasSize(1);
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("resource")
            .first()
            .as("authority resource")
            .isEqualTo("resource");
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("action")
            .first()
            .as("authority action")
            .isEqualTo("action");
    }

    @Test
    @DisplayName("When the user doesn't have authorities an exception is thrown")
    void testLoadByUsername_NoAuthorities() {
        final ThrowingCallable executable;
        final Exception        exception;

        given(userRepository.findOneByUsername("username")).willReturn(Optional.of(Users.enabled()));
        given(userGrantedPermissionRepository.findAllByUserId(1L)).willReturn(List.of());

        executable = () -> service.loadUserByUsername("username");

        exception = Assertions.catchThrowableOfType(executable, UsernameNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username username has no authorities");
    }

    @Test
    @DisplayName("When the user has the password expired it is returned")
    void testLoadByUsername_PasswordExpired() {
        final UserDetails    userDetails;
        final PersistentUser user;

        user = Users.passwordExpired();
        given(userRepository.findOneByUsername("username")).willReturn(Optional.of(user));
        given(userGrantedPermissionRepository.findAllByUserId(1L)).willReturn(List.of(getPersistentPermission()));

        userDetails = service.loadUserByUsername("username");

        Assertions.assertThat(userDetails.getUsername())
            .as("username")
            .isEqualTo("username");
        Assertions.assertThat(userDetails.getPassword())
            .as("password")
            .isEqualTo("1234");
        Assertions.assertThat(userDetails.isAccountNonExpired())
            .as("non expired")
            .isTrue();
        Assertions.assertThat(userDetails.isAccountNonLocked())
            .as("non locked")
            .isTrue();
        Assertions.assertThat(userDetails.isCredentialsNonExpired())
            .as("credentials non expired")
            .isFalse();
        Assertions.assertThat(userDetails.isEnabled())
            .as("enabled")
            .isTrue();

        Assertions.assertThat(userDetails.getAuthorities())
            .as("authorities size")
            .hasSize(1);
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("resource")
            .first()
            .as("authority resource")
            .isEqualTo("resource");
        Assertions.assertThat(userDetails.getAuthorities())
            .extracting("action")
            .first()
            .as("authority action")
            .isEqualTo("action");
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testLoadByUsername_UserNotExisting() {
        final ThrowingCallable executable;
        final Exception        exception;

        given(userRepository.findOneByUsername("username")).willReturn(Optional.empty());

        executable = () -> service.loadUserByUsername("username");

        exception = Assertions.catchThrowableOfType(executable, UsernameNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Username username not found in database");
    }

}
