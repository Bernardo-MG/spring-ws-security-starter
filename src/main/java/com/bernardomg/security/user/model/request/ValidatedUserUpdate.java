
package com.bernardomg.security.user.model.request;

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
public final class ValidatedUserUpdate implements UserUpdate {

    /**
     * User email.
     */
    @NotNull
    @Email
    private String  email;

    /**
     * User enabled flag.
     */
    @NotNull
    private Boolean enabled;

    /**
     * User id.
     */
    @NotNull
    private Long    id;

    /**
     * User name.
     */
    @NotNull
    private String  name;

    /**
     * Password expired flag.
     */
    @NotNull
    private Boolean passwordExpired;

}
