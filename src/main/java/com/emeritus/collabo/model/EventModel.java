package com.emeritus.collabo.model;

import org.bson.Document;
import org.springframework.data.annotation.Id;
import lombok.Data;

/**
 * The Class EventModel.
 */
/**
 * Instantiates a new event model.
 */

/**
 * Instantiates a new event model.
 */
@Data
public class EventModel {

  /** The id. */
  @Id
  private String id;

  /** The event. */
  private Document event;

}
