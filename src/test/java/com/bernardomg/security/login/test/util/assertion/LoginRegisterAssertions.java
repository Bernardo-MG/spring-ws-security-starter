
package com.bernardomg.security.login.test.util.assertion;

import org.assertj.core.api.SoftAssertions;

import com.bernardomg.security.login.model.LoginRegister;

public final class LoginRegisterAssertions {

    public static final void isEqualTo(final LoginRegister received, final LoginRegister expected) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(received.getUsername())
                .withFailMessage("Expected logged in '%s' but got '%s'", expected.getUsername(), received.getUsername())
                .isEqualTo(expected.getUsername());
            softly.assertThat(received.getDate())
                .withFailMessage("Expected date '%s' but got '%s'", expected.getDate(), received.getDate())
                .isEqualTo(expected.getDate());
            softly.assertThat(received.getUsername())
                .withFailMessage("Expected logged in '%s' but got '%s'", expected.getUsername(), received.getUsername())
                .isEqualTo(expected.getUsername());
        });
    }

}
