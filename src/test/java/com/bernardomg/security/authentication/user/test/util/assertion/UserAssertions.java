
package com.bernardomg.security.authentication.user.test.util.assertion;

import org.assertj.core.api.Assertions;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;

public final class UserAssertions {

    public static final void isEqualTo(final User received, final User expected) {
        Assertions.assertThat(received.getId())
            .withFailMessage("Expected id to not be null")
            .isNotNull();
        Assertions.assertThat(received.getUsername())
            .withFailMessage("Expected username id '%s' but got '%s'", expected.getUsername(), received.getUsername())
            .isEqualTo(expected.getUsername());
        Assertions.assertThat(received.getName())
            .withFailMessage("Expected name id '%s' but got '%s'", expected.getName(), received.getName())
            .isEqualTo(expected.getName());
        Assertions.assertThat(received.getEmail())
            .withFailMessage("Expected email '%s' but got '%s'", expected.getEmail(), received.getEmail())
            .isEqualTo(expected.getEmail());
        Assertions.assertThat(received.isPasswordExpired())
            .withFailMessage("Expected password expired flag '%s' but got '%s'", expected.isPasswordExpired(),
                received.isPasswordExpired())
            .isEqualTo(expected.isPasswordExpired());
        Assertions.assertThat(received.isEnabled())
            .withFailMessage("Expected enabled flag '%s' but got '%s'", expected.isEnabled(), received.isEnabled())
            .isEqualTo(expected.isEnabled());
        Assertions.assertThat(received.isExpired())
            .withFailMessage("Expected expired flag '%s' but got '%s'", expected.isExpired(), received.isExpired())
            .isEqualTo(expected.isExpired());
        Assertions.assertThat(received.isLocked())
            .withFailMessage("Expected locked flag '%s' but got '%s'", expected.isLocked(), received.isLocked())
            .isEqualTo(expected.isLocked());
    }

    public static final void isEqualTo(final UserEntity received, final UserEntity expected) {
        Assertions.assertThat(received.getId())
            .withFailMessage("Expected id to not be null")
            .isNotNull();
        Assertions.assertThat(received.getUsername())
            .withFailMessage("Expected username id '%s' but got '%s'", expected.getUsername(), received.getUsername())
            .isEqualTo(expected.getUsername());
        Assertions.assertThat(received.getName())
            .withFailMessage("Expected name id '%s' but got '%s'", expected.getName(), received.getName())
            .isEqualTo(expected.getName());
        Assertions.assertThat(received.getEmail())
            .withFailMessage("Expected email '%s' but got '%s'", expected.getEmail(), received.getEmail())
            .isEqualTo(expected.getEmail());
        Assertions.assertThat(received.getPassword())
            .withFailMessage("Expected password '%s' but got '%s'", expected.getPassword(), received.getPassword())
            .isEqualTo(expected.getPassword());
        Assertions.assertThat(received.getPasswordExpired())
            .withFailMessage("Expected password expired flag '%s' but got '%s'", expected.getPasswordExpired(),
                received.getPasswordExpired())
            .isEqualTo(expected.getPasswordExpired());
        Assertions.assertThat(received.getEnabled())
            .withFailMessage("Expected enabled flag '%s' but got '%s'", expected.getEnabled(), received.getEnabled())
            .isEqualTo(expected.getEnabled());
        Assertions.assertThat(received.getExpired())
            .withFailMessage("Expected expired flag '%s' but got '%s'", expected.getExpired(), received.getExpired())
            .isEqualTo(expected.getExpired());
        Assertions.assertThat(received.getLocked())
            .withFailMessage("Expected locked flag '%s' but got '%s'", expected.getLocked(), received.getLocked())
            .isEqualTo(expected.getLocked());
    }

}
