
package com.bernardomg.security.test.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.bernardomg.jwt.encoding.TokenDecoder;
import com.bernardomg.jwt.encoding.TokenEncoder;
import com.bernardomg.security.configuration.JwtAutoConfiguration;
import com.bernardomg.security.test.config.factory.JwtConstants;

@DisplayName("JwtAutoConfiguration")
final class JwtAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(JwtAutoConfiguration.class));

    @Test
    @DisplayName("Default beans are created")
    void test_DefaultBeans() {
        contextRunner.withPropertyValues("security.jwt.secret=" + JwtConstants.SECRET)
            .run(context -> {
                assertThat(context).hasNotFailed();

                assertSoftly(softly -> {
                    softly.assertThat(context.getBeansOfType(SecretKey.class))
                        .as("SecretKey beans")
                        .containsOnlyKeys("jwtSecretKey");

                    softly.assertThat(context.getBeansOfType(TokenDecoder.class))
                        .as("TokenDecoder beans")
                        .containsOnlyKeys("jwtTokenDecoder");

                    softly.assertThat(context.getBeansOfType(TokenEncoder.class))
                        .as("TokenEncoder beans")
                        .containsOnlyKeys("jwtTokenEncoder");
                });
            });
    }

    @Test
    @DisplayName("When the secret is missing, statup fails")
    void test_SecretIsMissing() {
        contextRunner.run(context -> {
            assertSoftly(softly -> {
                softly.assertThat(context.getStartupFailure())
                    .as("startup failure")
                    .isNotNull();

                softly.assertThat(context.getStartupFailure())
                    .as("startup failure message")
                    .hasMessageContaining("security.jwt");
            });
        });
    }

    @Test
    @DisplayName("When the secret is too short, statup fails")
    void test_SecretIsTooShort() {
        contextRunner.withPropertyValues("security.jwt.secret=" + JwtConstants.SECRET)
            .withPropertyValues("security.jwt.secret=short")
            .run(context -> {
                assertSoftly(softly -> {
                    softly.assertThat(context.getStartupFailure())
                        .as("startup failure")
                        .isNotNull();
                });
            });
    }

}
