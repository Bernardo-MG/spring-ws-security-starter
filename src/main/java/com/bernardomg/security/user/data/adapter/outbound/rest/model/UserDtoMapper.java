
package com.bernardomg.security.user.data.adapter.outbound.rest.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Sorting.Direction;
import com.bernardomg.data.domain.Sorting.Property;
import com.bernardomg.security.role.adapter.outbound.rest.model.RoleDtoMapper;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.ucronia.openapi.model.PropertyDto;
import com.bernardomg.ucronia.openapi.model.PropertyDto.DirectionEnum;
import com.bernardomg.ucronia.openapi.model.RoleDto;
import com.bernardomg.ucronia.openapi.model.SortingDto;
import com.bernardomg.ucronia.openapi.model.UserChangeDto;
import com.bernardomg.ucronia.openapi.model.UserDto;
import com.bernardomg.ucronia.openapi.model.UserPageResponseDto;
import com.bernardomg.ucronia.openapi.model.UserResponseDto;

public final class UserDtoMapper {

    public static final User toDomain(final UserChangeDto userChangeDto, final String username) {
        final Collection<Role> roles;

        if (userChangeDto.getRoles() == null) {
            roles = List.of();
        } else {
            roles = userChangeDto.getRoles()
                .stream()
                .map(r -> new Role(r, List.of()))
                .toList();
        }
        return new User(userChangeDto.getEmail(), username, userChangeDto.getName(), userChangeDto.getEnabled(),
            userChangeDto.getPasswordNotExpired(), false, false, roles);
    }

    public static final UserResponseDto toResponseDto(final Optional<User> user) {
        return new UserResponseDto().content(user.map(UserDtoMapper::toDto)
            .orElse(null));
    }

    public static final UserPageResponseDto toResponseDto(final Page<User> page) {
        final SortingDto sortingResponse;

        sortingResponse = new SortingDto().properties(page.sort()
            .properties()
            .stream()
            .map(UserDtoMapper::toDto)
            .toList());
        return new UserPageResponseDto().content(page.content()
            .stream()
            .map(UserDtoMapper::toDto)
            .toList())
            .size(page.size())
            .page(page.page())
            .totalElements(page.totalElements())
            .totalPages(page.totalPages())
            .elementsInPage(page.elementsInPage())
            .first(page.first())
            .last(page.last())
            .sort(sortingResponse);
    }

    public static final UserResponseDto toResponseDto(final User user) {
        return new UserResponseDto().content(toDto(user));
    }

    private static final PropertyDto toDto(final Property property) {
        final DirectionEnum direction;

        if (property.direction() == Direction.ASC) {
            direction = DirectionEnum.ASC;
        } else {
            direction = DirectionEnum.DESC;
        }
        return new PropertyDto().name(property.name())
            .direction(direction);
    }

    private static final UserDto toDto(final User user) {
        List<RoleDto> roles;

        roles = user.roles()
            .stream()
            .map(RoleDtoMapper::toDto)
            .toList();
        return new UserDto().email(user.email())
            .username(user.username())
            .name(user.name())
            .enabled(user.enabled())
            .notExpired(user.notExpired())
            .notLocked(user.notLocked())
            .passwordNotExpired(user.passwordNotExpired())
            .roles(roles);
    }

    private UserDtoMapper() {
        super();
    }

}
