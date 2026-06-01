package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Data for updating an existing user.
 */

@Schema(name = "UserChange", description = "Data for updating an existing user.")
@JsonTypeName("UserChange")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class UserChangeDto {

  private String email;

  private String name;

  private Boolean enabled;

  private Boolean passwordNotExpired;

  @Valid
  private List<String> roles = new ArrayList<>();

  public UserChangeDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserChangeDto(String email, String name, Boolean enabled, Boolean passwordNotExpired, List<String> roles) {
    this.email = email;
    this.name = name;
    this.enabled = enabled;
    this.passwordNotExpired = passwordNotExpired;
    this.roles = roles;
  }

  public UserChangeDto email(String email) {
    this.email = email;
    return this;
  }

  /**
   * New email address for the user.
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", description = "New email address for the user.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserChangeDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Display name of the user.
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Display name of the user.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserChangeDto enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Whether the user account is active.
   * @return enabled
   */
  @NotNull 
  @Schema(name = "enabled", description = "Whether the user account is active.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public UserChangeDto passwordNotExpired(Boolean passwordNotExpired) {
    this.passwordNotExpired = passwordNotExpired;
    return this;
  }

  /**
   * Whether the user's password is not expired.
   * @return passwordNotExpired
   */
  @NotNull 
  @Schema(name = "passwordNotExpired", description = "Whether the user's password is not expired.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("passwordNotExpired")
  public Boolean getPasswordNotExpired() {
    return passwordNotExpired;
  }

  public void setPasswordNotExpired(Boolean passwordNotExpired) {
    this.passwordNotExpired = passwordNotExpired;
  }

  public UserChangeDto roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public UserChangeDto addRolesItem(String rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * List of role names to assign to the user.
   * @return roles
   */
  @NotNull 
  @Schema(name = "roles", description = "List of role names to assign to the user.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roles")
  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserChangeDto userChange = (UserChangeDto) o;
    return Objects.equals(this.email, userChange.email) &&
        Objects.equals(this.name, userChange.name) &&
        Objects.equals(this.enabled, userChange.enabled) &&
        Objects.equals(this.passwordNotExpired, userChange.passwordNotExpired) &&
        Objects.equals(this.roles, userChange.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, name, enabled, passwordNotExpired, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserChangeDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    passwordNotExpired: ").append(toIndentedString(passwordNotExpired)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

