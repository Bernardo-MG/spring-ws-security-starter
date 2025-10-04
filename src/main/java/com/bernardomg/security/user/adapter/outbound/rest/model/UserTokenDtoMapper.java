
package com.bernardomg.security.user.adapter.outbound.rest.model;

import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Sorting.Direction;
import com.bernardomg.data.domain.Sorting.Property;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.model.UserTokenStatus;
import com.bernardomg.ucronia.openapi.model.PropertyDto;
import com.bernardomg.ucronia.openapi.model.PropertyDto.DirectionEnum;
import com.bernardomg.ucronia.openapi.model.SortingDto;
import com.bernardomg.ucronia.openapi.model.UserTokenChangeDto;
import com.bernardomg.ucronia.openapi.model.UserTokenDto;
import com.bernardomg.ucronia.openapi.model.UserTokenPageResponseDto;
import com.bernardomg.ucronia.openapi.model.UserTokenResponseDto;
import com.bernardomg.ucronia.openapi.model.UserTokenStatusDto;
import com.bernardomg.ucronia.openapi.model.UserTokenStatusResponseDto;

public final class UserTokenDtoMapper {

    public static UserToken toDomain(final UserTokenChangeDto userTokenChangeDto, final String token) {
        return new UserToken("", "", "", token, null, userTokenChangeDto.getExpirationDate(), null,
            userTokenChangeDto.getRevoked());
    }

    public static final UserTokenResponseDto toResponseDto(final Optional<UserToken> userToken) {
        return new UserTokenResponseDto().content(userToken.map(UserTokenDtoMapper::toDto)
            .orElse(null));
    }

    public static final UserTokenPageResponseDto toResponseDto(final Page<UserToken> page) {
        final SortingDto sortingResponse;

        sortingResponse = new SortingDto().properties(page.sort()
            .properties()
            .stream()
            .map(UserTokenDtoMapper::toDto)
            .toList());
        return new UserTokenPageResponseDto().content(page.content()
            .stream()
            .map(UserTokenDtoMapper::toDto)
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
