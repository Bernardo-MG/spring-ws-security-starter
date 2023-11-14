
package com.bernardomg.security.authorization.role.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UserRoleAddRequest implements UserRoleAdd {

    @NotNull
    private Long id;

}
