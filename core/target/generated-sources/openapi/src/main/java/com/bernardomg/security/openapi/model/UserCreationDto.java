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
 * UserCreationDto
 */

@JsonTypeName("UserCreation")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class UserCreationDto {

  private String email;

  private String username;

  private String name;

  @Valid
  private List<String> roles = new ArrayList<>();

  public UserCreationDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserCreationDto(String email, String username, String name, List<String> roles) {
    this.email = email;
    this.username = username;
    this.name = name;
    this.roles = roles;
  }

  public UserCreationDto email(String email) {
    this.email = email;
    return this;
  }

  /**
   * User email address.
   * @return email
   */
  @NotNull 
  @Schema(name = "email", description = "User email address.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserCreationDto username(String username) {
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

  public UserCreationDto name(String name) {
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

  public UserCreationDto roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public UserCreationDto addRolesItem(String rolesItem) {
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
  @NotNull 
  @Schema(name = "roles", description = "Roles assigned to the user.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    UserCreationDto userCreation = (UserCreationDto) o;
    return Objects.equals(this.email, userCreation.email) &&
        Objects.equals(this.username, userCreation.username) &&
        Objects.equals(this.name, userCreation.name) &&
        Objects.equals(this.roles, userCreation.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, username, name, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserCreationDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

