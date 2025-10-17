
package com.bernardomg.security.login.adapter.outbound.rest.model;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Sorting.Direction;
import com.bernardomg.data.domain.Sorting.Property;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.openapi.model.LoginRegisterDto;
import com.bernardomg.security.openapi.model.LoginRegisterPageResponseDto;
import com.bernardomg.security.openapi.model.PropertyDto;
import com.bernardomg.security.openapi.model.PropertyDto.DirectionEnum;
import com.bernardomg.security.openapi.model.SortingDto;

public final class LoginRegisterDtoMapper {

    public static final LoginRegisterPageResponseDto toResponseDto(final Page<LoginRegister> page) {
        final SortingDto sortingResponse;

        sortingResponse = new SortingDto().properties(page.sort()
            .properties()
            .stream()
            .map(LoginRegisterDtoMapper::toDto)
            .toList());

        return new LoginRegisterPageResponseDto().content(page.content()
            .stream()
            .map(LoginRegisterDtoMapper::toDto)
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

    private static final LoginRegisterDto toDto(final LoginRegister loginRegister) {
        return new LoginRegisterDto().username(loginRegister.username())
            .loggedIn(loginRegister.loggedIn())
            .date(loginRegister.date());
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

    private LoginRegisterDtoMapper() {
        super();
    }

}
