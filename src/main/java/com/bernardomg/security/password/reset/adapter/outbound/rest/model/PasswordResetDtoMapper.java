
package com.bernardomg.security.password.reset.adapter.outbound.rest.model;

import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.ucronia.openapi.model.UserTokenStatusDto;
import com.bernardomg.ucronia.openapi.model.UserTokenStatusResponseDto;

public final class PasswordResetDtoMapper {

    public static final UserTokenStatusResponseDto toResponseDto(final UserTokenStatus userTokenStatus) {
        UserTokenStatusDto userTokenStatusDto;

        userTokenStatusDto = new UserTokenStatusDto().username(userTokenStatus.username())
            .valid(userTokenStatus.valid());
        return new UserTokenStatusResponseDto().content(userTokenStatusDto);
    }

    private PasswordResetDtoMapper() {
        super();
    }

}
