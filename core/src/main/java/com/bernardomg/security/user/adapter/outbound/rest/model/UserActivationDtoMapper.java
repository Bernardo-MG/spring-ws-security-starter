
package com.bernardomg.security.user.adapter.outbound.rest.model;

import java.util.List;

import com.bernardomg.security.openapi.model.RoleDto;
import com.bernardomg.security.openapi.model.UserDto;
import com.bernardomg.security.openapi.model.UserResponseDto;
import com.bernardomg.security.role.adapter.outbound.rest.model.RoleDtoMapper;
import com.bernardomg.security.user.domain.model.User;

public final class UserActivationDtoMapper {

    public static final UserResponseDto toResponseDto(final User user) {
        final UserDto       userDto;
        final List<RoleDto> roles;

        roles = user.roles()
            .stream()
            .map(RoleDtoMapper::toDto)
            .toList();
        userDto = new UserDto().email(user.email())
            .username(user.username())
            .name(user.name())
            .enabled(user.enabled())
            .notExpired(user.notExpired())
            .notLocked(user.notLocked())
            .passwordNotExpired(user.passwordNotExpired())
            .roles(roles);
        return new UserResponseDto().content(userDto);
    }

    private UserActivationDtoMapper() {
        super();
    }

}
