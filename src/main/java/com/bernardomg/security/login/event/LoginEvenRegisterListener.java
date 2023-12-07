
package com.bernardomg.security.login.event;

import java.util.Objects;

import org.springframework.context.ApplicationListener;

import com.bernardomg.security.login.service.LoginRegisterService;

public final class LoginEvenRegisterListener implements ApplicationListener<LogInEvent> {

    private final LoginRegisterService loginRegisterService;

    public LoginEvenRegisterListener(final LoginRegisterService loginRegisterServ) {
        super();

        loginRegisterService = Objects.requireNonNull(loginRegisterServ);
    }

    @Override
    public final void onApplicationEvent(final LogInEvent event) {
        loginRegisterService.register(event.getUsername(), event.isLoggedIn());
    }

}
