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
 * Instantiates a new room response.
 *
 * @param roomId the room id
 * @param message the message
 */
@AllArgsConstructor

/**
 * Instantiates a new room response.
 */
@NoArgsConstructor

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class RoomResponse {

  /** The room id. */
  @JsonProperty("room_id")
  private String roomId;

  /** The message. */
  private String message;

}
