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
 * Instantiates a new invite user request.
 *
 * @param userId the user id
 * @param reason the reason
 */
@AllArgsConstructor

/**
 * Instantiates a new invite user request.
 */
@NoArgsConstructor

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class InviteUserRequest {

  /** The user id. */
  @JsonProperty("user_id")
  private String userId;

  /** The reason. */
  private String reason;

}
