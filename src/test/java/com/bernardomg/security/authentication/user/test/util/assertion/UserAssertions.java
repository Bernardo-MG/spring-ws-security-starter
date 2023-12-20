
package com.bernardomg.security.authentication.user.test.util.assertion;

import org.assertj.core.api.SoftAssertions;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;

public final class UserAssertions {

    public static final void isEqualTo(final User received, final User expected) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(received.getUsername())
                .withFailMessage("Expected username '%s' but got '%s'", expected.getUsername(), received.getUsername())
                .isEqualTo(expected.getUsername());
            softly.assertThat(received.getName())
                .withFailMessage("Expected name '%s' but got '%s'", expected.getName(), received.getName())
                .isEqualTo(expected.getName());
            softly.assertThat(received.getEmail())
                .withFailMessage("Expected email '%s' but got '%s'", expected.getEmail(), received.getEmail())
                .isEqualTo(expected.getEmail());
            softly.assertThat(received.isPasswordExpired())
                .withFailMessage("Expected password expired flag '%s' but got '%s'", expected.isPasswordExpired(),
                    received.isPasswordExpired())
                .isEqualTo(expected.isPasswordExpired());
            softly.assertThat(received.isEnabled())
                .withFailMessage("Expected enabled flag '%s' but got '%s'", expected.isEnabled(), received.isEnabled())
                .isEqualTo(expected.isEnabled());
            softly.assertThat(received.isExpired())
                .withFailMessage("Expected expired flag '%s' but got '%s'", expected.isExpired(), received.isExpired())
                .isEqualTo(expected.isExpired());
            softly.assertThat(received.isLocked())
                .withFailMessage("Expected locked flag '%s' but got '%s'", expected.isLocked(), received.isLocked())
                .isEqualTo(expected.isLocked());
        });
    }

    public static final void isEqualTo(final UserEntity received, final UserEntity expected) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(received.getId())
                .withFailMessage("Expected id to not be null")
                .isNotNull();
            softly.assertThat(received.getUsername())
                .withFailMessage("Expected username '%s' but got '%s'", expected.getUsername(), received.getUsername())
                .isEqualTo(expected.getUsername());
            softly.assertThat(received.getName())
                .withFailMessage("Expected name '%s' but got '%s'", expected.getName(), received.getName())
                .isEqualTo(expected.getName());
            softly.assertThat(received.getEmail())
                .withFailMessage("Expected email '%s' but got '%s'", expected.getEmail(), received.getEmail())
                .isEqualTo(expected.getEmail());
            softly.assertThat(received.getPassword())
                .withFailMessage("Expected password '%s' but got '%s'", expected.getPassword(), received.getPassword())
                .isEqualTo(expected.getPassword());
            softly.assertThat(received.getPasswordExpired())
                .withFailMessage("Expected password expired flag '%s' but got '%s'", expected.getPasswordExpired(),
                    received.getPasswordExpired())
                .isEqualTo(expected.getPasswordExpired());
            softly.assertThat(received.getEnabled())
                .withFailMessage("Expected enabled flag '%s' but got '%s'", expected.getEnabled(),
                    received.getEnabled())
                .isEqualTo(expected.getEnabled());
            softly.assertThat(received.getExpired())
                .withFailMessage("Expected expired flag '%s' but got '%s'", expected.getExpired(),
                    received.getExpired())
                .isEqualTo(expected.getExpired());
            softly.assertThat(received.getLocked())
                .withFailMessage("Expected locked flag '%s' but got '%s'", expected.getLocked(), received.getLocked())
                .isEqualTo(expected.getLocked());
        });
    }

}
