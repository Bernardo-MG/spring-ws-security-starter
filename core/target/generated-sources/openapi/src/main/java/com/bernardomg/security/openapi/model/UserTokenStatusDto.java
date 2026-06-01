package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserTokenStatusDto
 */

@JsonTypeName("UserTokenStatus")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class UserTokenStatusDto {

  private String username;

  private Boolean valid;

  public UserTokenStatusDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserTokenStatusDto(String username, Boolean valid) {
    this.username = username;
    this.valid = valid;
  }

  public UserTokenStatusDto username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Username associated with the token.
   * @return username
   */
  @NotNull 
  @Schema(name = "username", description = "Username associated with the token.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserTokenStatusDto valid(Boolean valid) {
    this.valid = valid;
    return this;
  }

  /**
   * Indicates if the token is valid.
   * @return valid
   */
  @NotNull 
  @Schema(name = "valid", description = "Indicates if the token is valid.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("valid")
  public Boolean getValid() {
    return valid;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserTokenStatusDto userTokenStatus = (UserTokenStatusDto) o;
    return Objects.equals(this.username, userTokenStatus.username) &&
        Objects.equals(this.valid, userTokenStatus.valid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, valid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserTokenStatusDto {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
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

