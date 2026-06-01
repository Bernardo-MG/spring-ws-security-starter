
package com.bernardomg.security.login.test.usecase.service.unit;

import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;
import com.bernardomg.security.login.usecase.service.DefaultLoginRegisterService;
import com.bernardomg.security.user.test.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginRegisterService - register log in")
class TestLoginRegisterServiceRegister {

    private final Instant                 dayEnd   = LocalDate.now()
        .plusDays(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    private final Instant                 dayStart = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    @Captor
    private ArgumentCaptor<LoginRegister> loginRegisterCaptor;

    @Mock
    private LoginRegisterRepository       loginRegisterRepository;

    @InjectMocks
    private DefaultLoginRegisterService   service;

    @Test
    @DisplayName("Persists a succesful log in attempt")
    void testRegister_Logged_Persisted() {
        final LoginRegister register;

        // WHEN
        service.register(UserConstants.USERNAME, true);

        // THEN
        verify(loginRegisterRepository).save(loginRegisterCaptor.capture());

        register = loginRegisterCaptor.getValue();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(register.loggedIn())
                .as("logged in")
                .isTrue();
            softly.assertThat(register.username())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);

            softly.assertThat(register.date())
                .as("date")
                .isBetween(dayStart, dayEnd);
        });
    }

    @Test
    @DisplayName("Persists a failed log in attempt")
    void testRegister_NotLogged_Persisted() {
        final LoginRegister register;

        // WHEN
        service.register(UserConstants.USERNAME, false);

        // THEN
        verify(loginRegisterRepository).save(loginRegisterCaptor.capture());

        register = loginRegisterCaptor.getValue();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(register.loggedIn())
                .as("logged in")
                .isFalse();
            softly.assertThat(register.username())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);

            softly.assertThat(register.date())
                .as("date")
                .isBetween(dayStart, dayEnd);
        });
    }

}
