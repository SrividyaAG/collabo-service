package com.emeritus.collabo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new access token response.
 *
 * @param userId the user id
 * @param accessToken the access token
 */
@AllArgsConstructor

/**
 * Instantiates a new access token response.
 */
@NoArgsConstructor

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class AccessTokenResponse {

  /** The user id. */
  @JsonProperty("user_id")
  private String userId;

  /** The access token. */
  @JsonProperty("access_token")
  private String accessToken;

}
