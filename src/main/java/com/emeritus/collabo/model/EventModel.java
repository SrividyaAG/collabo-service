package com.emeritus.collabo.model;

import org.bson.Document;
import org.springframework.data.annotation.Id;
import lombok.Data;

/**
 * The Class EventModel.
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "events")

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
