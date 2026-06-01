package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.Instant;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LoginRegisterDto
 */

@JsonTypeName("LoginRegister")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class LoginRegisterDto {

  private String username;

  private Boolean loggedIn;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant date;

  public LoginRegisterDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoginRegisterDto(String username, Boolean loggedIn, Instant date) {
    this.username = username;
    this.loggedIn = loggedIn;
    this.date = date;
  }

  public LoginRegisterDto username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Unique username.
   * @return username
   */
  @NotNull 
  @Schema(name = "username", description = "Unique username.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public LoginRegisterDto loggedIn(Boolean loggedIn) {
    this.loggedIn = loggedIn;
    return this;
  }

  /**
   * Succesful, or not, log in attempt.
   * @return loggedIn
   */
  @NotNull 
  @Schema(name = "loggedIn", description = "Succesful, or not, log in attempt.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loggedIn")
  public Boolean getLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(Boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public LoginRegisterDto date(Instant date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
   */
  @NotNull @Valid 
  @Schema(name = "date", example = "2025-08-01T00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("date")
  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginRegisterDto loginRegister = (LoginRegisterDto) o;
    return Objects.equals(this.username, loginRegister.username) &&
        Objects.equals(this.loggedIn, loginRegister.loggedIn) &&
        Objects.equals(this.date, loginRegister.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, loggedIn, date);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginRegisterDto {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    loggedIn: ").append(toIndentedString(loggedIn)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
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

