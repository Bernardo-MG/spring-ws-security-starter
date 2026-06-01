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
 * TokenLoginStatusDto
 */

@JsonTypeName("TokenLoginStatus")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class TokenLoginStatusDto {

  private Boolean logged;

  private String token;

  public TokenLoginStatusDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TokenLoginStatusDto(Boolean logged, String token) {
    this.logged = logged;
    this.token = token;
  }

  public TokenLoginStatusDto logged(Boolean logged) {
    this.logged = logged;
    return this;
  }

  /**
   * Logged in status.
   * @return logged
   */
  @NotNull 
  @Schema(name = "logged", description = "Logged in status.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("logged")
  public Boolean getLogged() {
    return logged;
  }

  public void setLogged(Boolean logged) {
    this.logged = logged;
  }

  public TokenLoginStatusDto token(String token) {
    this.token = token;
    return this;
  }

  /**
   * User auth token.
   * @return token
   */
  @NotNull 
  @Schema(name = "token", description = "User auth token.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenLoginStatusDto tokenLoginStatus = (TokenLoginStatusDto) o;
    return Objects.equals(this.logged, tokenLoginStatus.logged) &&
        Objects.equals(this.token, tokenLoginStatus.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logged, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TokenLoginStatusDto {\n");
    sb.append("    logged: ").append(toIndentedString(logged)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

