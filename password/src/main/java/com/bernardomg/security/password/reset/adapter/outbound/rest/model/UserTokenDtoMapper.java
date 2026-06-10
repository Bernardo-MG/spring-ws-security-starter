
package com.bernardomg.security.password.reset.adapter.outbound.rest.model;

import com.bernardomg.security.password.adapter.outbound.rest.dto.UserTokenStatusDto;
import com.bernardomg.security.password.adapter.outbound.rest.dto.UserTokenStatusResponseDto;
import com.bernardomg.security.user.domain.model.UserTokenStatus;

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
