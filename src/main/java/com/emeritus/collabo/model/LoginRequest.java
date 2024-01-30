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
 * Instantiates a new login request.
 *
 * @param type the type
 * @param identifier the identifier
 * @param password the password
 */
@AllArgsConstructor

/**
 * Instantiates a new login request.
 */
@NoArgsConstructor

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class LoginRequest {

  /** The type. */
  private String type;

  /** The identifier. */
  private Identifier identifier;

  /** The password. */
  private String password;

}
