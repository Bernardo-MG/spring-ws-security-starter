
package com.bernardomg.security.permission.adapter.outbound.rest.model;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Sorting.Direction;
import com.bernardomg.data.domain.Sorting.Property;
import com.bernardomg.security.openapi.model.PropertyDto;
import com.bernardomg.security.openapi.model.PropertyDto.DirectionEnum;
import com.bernardomg.security.openapi.model.ResourcePermissionDto;
import com.bernardomg.security.openapi.model.ResourcePermissionPageResponseDto;
import com.bernardomg.security.openapi.model.SortingDto;
import com.bernardomg.security.permission.domain.model.ResourcePermission;

public final class PermissionDtoMapper {

    public static final ResourcePermissionPageResponseDto toResponseDto(final Page<ResourcePermission> page) {
        final SortingDto sortingResponse;

        sortingResponse = new SortingDto().properties(page.sort()
            .properties()
            .stream()
            .map(PermissionDtoMapper::toDto)
            .toList());
        return new ResourcePermissionPageResponseDto().content(page.content()
            .stream()
            .map(PermissionDtoMapper::toDto)
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

    private static final ResourcePermissionDto toDto(final ResourcePermission permission) {
        return new ResourcePermissionDto().resource(permission.resource())
            .action(permission.action())
            .name(permission.getName());
    }

    private PermissionDtoMapper() {
        super();
    }

}
