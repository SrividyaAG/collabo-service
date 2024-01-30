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
 * To string.
 *
 * @return the java.lang. string
 */
@Builder

/**
 * Instantiates a new removes the user request.
 */
@NoArgsConstructor

/**
 * Instantiates a new removes the user request.
 *
 * @param userId the user id
 * @param reason the reason
 */
@AllArgsConstructor
public class RemoveUserRequest {

  
  /** The user id. */
  @JsonProperty("user_id")
  private String userId;

  /** The reason. */
  private String reason;

}
