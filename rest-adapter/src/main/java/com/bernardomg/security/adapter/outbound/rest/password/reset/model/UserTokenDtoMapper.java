
package com.bernardomg.security.adapter.outbound.rest.password.reset.model;

import com.bernardomg.security.domain.user.model.UserTokenStatus;
import com.bernardomg.security.password.adapter.outbound.rest.dto.UserTokenStatusDto;
import com.bernardomg.security.password.adapter.outbound.rest.dto.UserTokenStatusResponseDto;

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
