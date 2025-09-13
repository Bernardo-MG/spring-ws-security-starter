
package com.bernardomg.security.login.adapter.outbound.rest.model;

import java.util.List;

import com.bernardomg.data.domain.Page;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.ucronia.openapi.model.LoginRegisterDto;
import com.bernardomg.ucronia.openapi.model.LoginRegisterPageResponseDto;

public final class LoginRegisterDtoMapper {

    public static final LoginRegisterPageResponseDto toResponseDto(final Page<LoginRegister> loginRegisters) {
        List<LoginRegisterDto> loginRegisterDtos;

        loginRegisterDtos = loginRegisters.content()
            .stream()
            .map(LoginRegisterDtoMapper::toDto)
            .toList();
        return new LoginRegisterPageResponseDto().content(loginRegisterDtos);
    }

    private static final LoginRegisterDto toDto(final LoginRegister loginRegister) {
        return new LoginRegisterDto().username(loginRegister.username())
            .loggedIn(loginRegister.loggedIn())
            .date(loginRegister.date());
    }

    private LoginRegisterDtoMapper() {
        super();
    }

}
