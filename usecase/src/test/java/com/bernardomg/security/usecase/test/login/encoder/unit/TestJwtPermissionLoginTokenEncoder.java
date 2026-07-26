
package com.bernardomg.security.usecase.test.login.encoder.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.jwt.encoding.TokenEncoder;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.login.encoder.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtPermissionLoginTokenEncoder")
class TestJwtPermissionLoginTokenEncoder {

    @InjectMocks
    private JwtPermissionLoginTokenEncoder encoder;

    private final String                   TOKEN = "token";

    @Mock
    private TokenEncoder                   tokenEncoder;

    @Mock
    private UserRepository                 userRepository;

    @Mock
    private Duration                       validity;

    public TestJwtPermissionLoginTokenEncoder() {
        super();
        // TODO: verify the permissions are sent to the encoder
    }

    @Test
    @DisplayName("Returns the generated token")
    void testEncode_NoData() {
        final String token;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(tokenEncoder.encode(any())).willReturn(TOKEN);

        // WHEN
        token = encoder.encode(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEqualTo(TOKEN);
    }

}
