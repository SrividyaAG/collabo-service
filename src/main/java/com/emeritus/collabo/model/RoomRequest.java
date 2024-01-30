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
 * Instantiates a new room request.
 *
 * @param preset the preset
 * @param roomAliasName the room alias name
 * @param topic the topic
 * @param name the name
 * @param creationContent the creation content
 */
@AllArgsConstructor

/**
 * Instantiates a new room request.
 */
@NoArgsConstructor

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class RoomRequest {

  /** The preset. */
  private String preset;

  /** The room alias name. */
  @JsonProperty("room_alias_name")
  private String roomAliasName;

  /** The topic. */
  private String topic;

  /** The name. */
  private String name;

  /** The creation content. */
  @JsonProperty("creation_content")
  private CreationContent creationContent;

}
