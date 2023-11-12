
package com.bernardomg.security.authentication.user.model.query;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UserCreateRequest implements UserCreate {

    /**
     * User email.
     */
    @NotNull
    @Email
    private String email;

    /**
     * User name.
     */
    @NotNull
    private String name;

    /**
     * User username.
     */
    @NotNull
    private String username;

}
