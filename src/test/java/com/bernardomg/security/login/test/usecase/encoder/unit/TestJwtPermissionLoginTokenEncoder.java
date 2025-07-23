
package com.bernardomg.security.login.test.usecase.encoder.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.jwt.encoding.TokenEncoder;
import com.bernardomg.security.login.usecase.encoder.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.user.permission.domain.repository.UserPermissionRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtPermissionLoginTokenEncoder")
class TestJwtPermissionLoginTokenEncoder {

    @InjectMocks
    private JwtPermissionLoginTokenEncoder encoder;

    private final String                   TOKEN = "token";

    @Mock
    private TokenEncoder                   tokenEncoder;

    @Mock
    private UserPermissionRepository       userPermissionRepository;

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
        given(userPermissionRepository.findAll(UserConstants.USERNAME)).willReturn(List.of());
        given(tokenEncoder.encode(any())).willReturn(TOKEN);

        // WHEN
        token = encoder.encode(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEqualTo(TOKEN);
    }

}
