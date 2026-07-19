
package com.bernardomg.security.adapter.outbound.rest.login.model;

import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.domain.login.model.TokenLoginStatus;
import com.bernardomg.security.login.adapter.outbound.rest.dto.LoginRequestDto;
import com.bernardomg.security.login.adapter.outbound.rest.dto.TokenLoginStatusDto;
import com.bernardomg.security.login.adapter.outbound.rest.dto.TokenLoginStatusResponseDto;

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
