
package com.bernardomg.security.springframework.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

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

import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.springframework.test.permission.config.factory.PermissionConstants;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.springframework.test.user.config.factory.Users;
import com.bernardomg.security.springframework.usecase.service.UserDomainDetailsService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDomainDetailsService")
class TestUserDomainDetailsService {

    @InjectMocks
    private UserDomainDetailsService service;

    @Mock
    private UserRepository           userRepository;

    public TestUserDomainDetailsService() {
        super();
    }

    @Test
    @DisplayName("When the user is disabled it is returned")
    void testLoadByUsername_Disabled() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.disabled()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));

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
                .isEqualTo(PermissionConstants.CREATE);
        });
    }

    @Test
    @DisplayName("When logging with an email the user details are returned")
    void testLoadByUsername_Email() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.EMAIL);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userDetails.getUsername())
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(userDetails.getPassword())
                .isEqualTo(UserConstants.PASSWORD);
            softly.assertThat(userDetails.isEnabled())
                .isTrue();
            softly.assertThat(userDetails.isAccountNonExpired())
                .isTrue();
            softly.assertThat(userDetails.isAccountNonLocked())
                .isTrue();
            softly.assertThat(userDetails.isCredentialsNonExpired())
                .isTrue();
        });
    }

    @Test
    @DisplayName("When the user is enabled it is returned")
    void testLoadByUsername_Enabled() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));

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
                .isEqualTo(PermissionConstants.CREATE);
        });
    }

    @Test
    @DisplayName("When the user is expired it is returned")
    void testLoadByUsername_Expired() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.expired()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));

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
                .isEqualTo(PermissionConstants.CREATE);
        });
    }

    @Test
    @DisplayName("When the user is locked it is returned")
    void testLoadByUsername_Locked() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.locked()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));

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
                .isEqualTo(PermissionConstants.CREATE);
        });
    }

    @Test
    @DisplayName("When the user doesn't have authorities an exception is thrown")
    void testLoadByUsername_NoAuthorities() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        executable = () -> service.loadUserByUsername(UserConstants.USERNAME);

        // THEN
        exception = Assertions.catchThrowableOfType(UsernameNotFoundException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Invalid username or credentials");
    }

    @Test
    @DisplayName("When the user has the password expired it is returned")
    void testLoadByUsername_PasswordExpired() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpired()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));

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
                .isEqualTo(PermissionConstants.CREATE);
        });
    }

    @Test
    @DisplayName("When the username is in uppercase it is returned")
    void testLoadByUsername_UpperCase() {
        final UserDetails userDetails;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userRepository.findPassword(UserConstants.USERNAME)).willReturn(Optional.of(UserConstants.PASSWORD));

        // WHEN
        userDetails = service.loadUserByUsername(UserConstants.USERNAME.toUpperCase());

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
                .isEqualTo(PermissionConstants.CREATE);
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
        exception = Assertions.catchThrowableOfType(UsernameNotFoundException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Invalid username or credentials");
    }

}
