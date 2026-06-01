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
 * PasswordChangeDto
 */

@JsonTypeName("PasswordChange")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class PasswordChangeDto {

  private String oldPassword;

  private String newPassword;

  public PasswordChangeDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PasswordChangeDto(String oldPassword, String newPassword) {
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public PasswordChangeDto oldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
    return this;
  }

  /**
   * Current password of the user.
   * @return oldPassword
   */
  @NotNull 
  @Schema(name = "oldPassword", description = "Current password of the user.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("oldPassword")
  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public PasswordChangeDto newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * New password to set for the user.
   * @return newPassword
   */
  @NotNull 
  @Schema(name = "newPassword", description = "New password to set for the user.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newPassword")
  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasswordChangeDto passwordChange = (PasswordChangeDto) o;
    return Objects.equals(this.oldPassword, passwordChange.oldPassword) &&
        Objects.equals(this.newPassword, passwordChange.newPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oldPassword, newPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PasswordChangeDto {\n");
    sb.append("    oldPassword: ").append("*").append("\n");
    sb.append("    newPassword: ").append("*").append("\n");
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

