
package com.bernardomg.security.adapter.outbound.rest.login.model;

import com.bernardomg.security.adapter.outbound.rest.login.dto.LoginRequestDto;
import com.bernardomg.security.adapter.outbound.rest.login.dto.TokenLoginStatusDto;
import com.bernardomg.security.adapter.outbound.rest.login.dto.TokenLoginStatusResponseDto;
import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.domain.login.model.TokenLoginStatus;

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
