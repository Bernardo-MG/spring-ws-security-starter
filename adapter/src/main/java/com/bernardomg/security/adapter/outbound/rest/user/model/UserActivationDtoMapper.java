
package com.bernardomg.security.adapter.outbound.rest.user.model;

import java.util.List;

import com.bernardomg.security.adapter.outbound.rest.role.domain.RoleDtoMapper;
import com.bernardomg.security.adapter.outbound.rest.user.dto.RoleDto;
import com.bernardomg.security.adapter.outbound.rest.user.dto.UserDto;
import com.bernardomg.security.adapter.outbound.rest.user.dto.UserResponseDto;
import com.bernardomg.security.domain.user.model.User;

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
