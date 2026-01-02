
package com.bernardomg.security.user.adapter.outbound.rest.model;

import java.util.List;
import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Sorting.Direction;
import com.bernardomg.data.domain.Sorting.Property;
import com.bernardomg.security.openapi.model.PropertyDto;
import com.bernardomg.security.openapi.model.PropertyDto.DirectionEnum;
import com.bernardomg.security.openapi.model.SortingDto;
import com.bernardomg.security.openapi.model.UserTokenChangeDto;
import com.bernardomg.security.openapi.model.UserTokenDto;
import com.bernardomg.security.openapi.model.UserTokenPageResponseDto;
import com.bernardomg.security.openapi.model.UserTokenResponseDto;
import com.bernardomg.security.openapi.model.UserTokenStatusDto;
import com.bernardomg.security.openapi.model.UserTokenStatusResponseDto;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.model.UserTokenStatus;

public final class UserTokenDtoMapper {

    public static UserToken toDomain(final UserTokenChangeDto userTokenChangeDto, final String token) {
        return new UserToken("", "", "", token, null, userTokenChangeDto.getExpirationDate(), null,
            userTokenChangeDto.getRevoked());
    }

    public static final UserTokenResponseDto toResponseDto(final Optional<UserToken> userToken) {
        final UserTokenDto content;

        content = userToken.map(UserTokenDtoMapper::toDto)
            .orElse(null);
        return new UserTokenResponseDto().content(content);
    }

    public static final UserTokenPageResponseDto toResponseDto(final Page<UserToken> page) {
        final SortingDto         sortingResponse;
        final List<UserTokenDto> content;

        sortingResponse = new SortingDto().properties(page.sort()
            .properties()
            .stream()
            .map(UserTokenDtoMapper::toDto)
            .toList());
        content = page.content()
            .stream()
            .map(UserTokenDtoMapper::toDto)
            .toList();
        return new UserTokenPageResponseDto().content(content)
            .size(page.size())
            .page(page.page())
            .totalElements(page.totalElements())
            .totalPages(page.totalPages())
            .elementsInPage(page.elementsInPage())
            .first(page.first())
            .last(page.last())
            .sort(sortingResponse);
    }

    public static final UserTokenResponseDto toResponseDto(final UserToken userToken) {
        return new UserTokenResponseDto().content(toDto(userToken));
    }

    public static final UserTokenStatusResponseDto toResponseDto(final UserTokenStatus userTokenStatus) {
        UserTokenStatusDto userTokenStatusDto;

        userTokenStatusDto = new UserTokenStatusDto().username(userTokenStatus.username())
            .valid(userTokenStatus.valid());
        return new UserTokenStatusResponseDto().content(userTokenStatusDto);
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

    private static final UserTokenDto toDto(final UserToken userToken) {
        return new UserTokenDto().username(userToken.username())
            .name(userToken.name())
            .scope(userToken.scope())
            .creationDate(userToken.creationDate())
            .expirationDate(userToken.expirationDate())
            .consumed(userToken.consumed())
            .revoked(userToken.revoked())
            .token(userToken.token());
    }

    private UserTokenDtoMapper() {
        super();
    }

}
