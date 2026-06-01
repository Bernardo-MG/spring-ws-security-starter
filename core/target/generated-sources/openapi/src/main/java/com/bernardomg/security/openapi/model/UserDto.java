package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.bernardomg.security.openapi.model.RoleDto;
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
 * System user and their account status.
 */

@Schema(name = "User", description = "System user and their account status.")
@JsonTypeName("User")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class UserDto {

  private String email;

  private String username;

  private String name;

  private Boolean enabled;

  private Boolean notExpired;

  private Boolean notLocked;

  private Boolean passwordNotExpired;

  @Valid
  private List<@Valid RoleDto> roles = new ArrayList<>();

  public UserDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserDto(String email, String username, String name, Boolean enabled, Boolean notExpired, Boolean notLocked, Boolean passwordNotExpired, List<@Valid RoleDto> roles) {
    this.email = email;
    this.username = username;
    this.name = name;
    this.enabled = enabled;
    this.notExpired = notExpired;
    this.notLocked = notLocked;
    this.passwordNotExpired = passwordNotExpired;
    this.roles = roles;
  }

  public UserDto email(String email) {
    this.email = email;
    return this;
  }

  /**
   * User email address.
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", description = "User email address.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserDto username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Unique username for the user.
   * @return username
   */
  @NotNull 
  @Schema(name = "username", description = "Unique username for the user.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserDto name(String name) {
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

  public UserDto enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Indicates if the account is currently enabled.
   * @return enabled
   */
  @NotNull 
  @Schema(name = "enabled", description = "Indicates if the account is currently enabled.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public UserDto notExpired(Boolean notExpired) {
    this.notExpired = notExpired;
    return this;
  }

  /**
   * True if the account is not expired.
   * @return notExpired
   */
  @NotNull 
  @Schema(name = "notExpired", description = "True if the account is not expired.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("notExpired")
  public Boolean getNotExpired() {
    return notExpired;
  }

  public void setNotExpired(Boolean notExpired) {
    this.notExpired = notExpired;
  }

  public UserDto notLocked(Boolean notLocked) {
    this.notLocked = notLocked;
    return this;
  }

  /**
   * True if the account is not locked.
   * @return notLocked
   */
  @NotNull 
  @Schema(name = "notLocked", description = "True if the account is not locked.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("notLocked")
  public Boolean getNotLocked() {
    return notLocked;
  }

  public void setNotLocked(Boolean notLocked) {
    this.notLocked = notLocked;
  }

  public UserDto passwordNotExpired(Boolean passwordNotExpired) {
    this.passwordNotExpired = passwordNotExpired;
    return this;
  }

  /**
   * True if the password is still valid (not expired).
   * @return passwordNotExpired
   */
  @NotNull 
  @Schema(name = "passwordNotExpired", description = "True if the password is still valid (not expired).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("passwordNotExpired")
  public Boolean getPasswordNotExpired() {
    return passwordNotExpired;
  }

  public void setPasswordNotExpired(Boolean passwordNotExpired) {
    this.passwordNotExpired = passwordNotExpired;
  }

  public UserDto roles(List<@Valid RoleDto> roles) {
    this.roles = roles;
    return this;
  }

  public UserDto addRolesItem(RoleDto rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Roles assigned to the user.
   * @return roles
   */
  @NotNull @Valid 
  @Schema(name = "roles", description = "Roles assigned to the user.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roles")
  public List<@Valid RoleDto> getRoles() {
    return roles;
  }

  public void setRoles(List<@Valid RoleDto> roles) {
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
    UserDto user = (UserDto) o;
    return Objects.equals(this.email, user.email) &&
        Objects.equals(this.username, user.username) &&
        Objects.equals(this.name, user.name) &&
        Objects.equals(this.enabled, user.enabled) &&
        Objects.equals(this.notExpired, user.notExpired) &&
        Objects.equals(this.notLocked, user.notLocked) &&
        Objects.equals(this.passwordNotExpired, user.passwordNotExpired) &&
        Objects.equals(this.roles, user.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, username, name, enabled, notExpired, notLocked, passwordNotExpired, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    notExpired: ").append(toIndentedString(notExpired)).append("\n");
    sb.append("    notLocked: ").append(toIndentedString(notLocked)).append("\n");
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

