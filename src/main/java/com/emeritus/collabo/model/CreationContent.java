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
 * Instantiates a new creation content.
 *
 * @param federate the federate
 */
@AllArgsConstructor

/**
 * Instantiates a new creation content.
 */
@NoArgsConstructor

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class CreationContent {

  /** The federate. */
  @JsonProperty("m.federate")
  private boolean federate;

}
