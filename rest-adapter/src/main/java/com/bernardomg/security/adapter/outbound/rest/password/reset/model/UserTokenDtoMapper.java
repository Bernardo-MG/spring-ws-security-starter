
package com.bernardomg.security.adapter.outbound.rest.password.reset.model;

import com.bernardomg.security.adapter.outbound.rest.password.dto.UserTokenStatusDto;
import com.bernardomg.security.adapter.outbound.rest.password.dto.UserTokenStatusResponseDto;
import com.bernardomg.security.domain.user.model.UserTokenStatus;

public final class UserTokenDtoMapper {

    public static final UserTokenStatusResponseDto toResponseDto(final UserTokenStatus userTokenStatus) {
        UserTokenStatusDto userTokenStatusDto;

        userTokenStatusDto = new UserTokenStatusDto().username(userTokenStatus.username())
            .valid(userTokenStatus.valid());
        return new UserTokenStatusResponseDto().content(userTokenStatusDto);
    }

    private UserTokenDtoMapper() {
        super();
    }

}
