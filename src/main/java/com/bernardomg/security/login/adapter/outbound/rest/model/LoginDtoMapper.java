
package com.bernardomg.security.login.adapter.outbound.rest.model;

import com.bernardomg.security.login.domain.model.Credentials;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.openapi.model.LoginRequestDto;
import com.bernardomg.security.openapi.model.TokenLoginStatusDto;
import com.bernardomg.security.openapi.model.TokenLoginStatusResponseDto;

public final class LoginDtoMapper {

    public static final Credentials toDomain(final LoginRequestDto loginRequestDto) {
        return new Credentials(loginRequestDto.getUsername(), loginRequestDto.getPassword());
    }

    public static final TokenLoginStatusResponseDto toResponseDto(final TokenLoginStatus tokenLoginStatus) {
        TokenLoginStatusDto tokenLoginStatusDto;

        tokenLoginStatusDto = new TokenLoginStatusDto().logged(tokenLoginStatus.logged())
            .token(tokenLoginStatus.token());
        return new TokenLoginStatusResponseDto().content(tokenLoginStatusDto);
    }

    private LoginDtoMapper() {
        super();
    }

}
