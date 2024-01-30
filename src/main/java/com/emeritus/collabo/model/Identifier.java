package com.emeritus.collabo.model;

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
 * Instantiates a new identifier.
 *
 * @param type the type
 * @param user the user
 */
@AllArgsConstructor

/**
 * Instantiates a new identifier.
 */
@NoArgsConstructor

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class Identifier {

  /** The type. */
  private String type;

  /** The user. */
  private String user;

}
